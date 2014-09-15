/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.google.maps.internal;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Rate Limit Policy for Google Maps Web Services APIs.
 */
public class RateLimitExecutorService implements ExecutorService {

  private static final Logger log = Logger.getLogger(RateLimitExecutorService.class.getName());

  // It's important we set Ok's second arg to threadFactory(.., true) to ensure the threads are
  // killed when the app exits. For synchronous requests this is ideal but it means any async
  // requests still pending after termination will be killed.
  private final ExecutorService delegate = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60,
      TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
      threadFactory("Rate Limited Dispatcher", true));

  private final Thread delayThread;

  private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

  public RateLimitExecutorService(final int queriesPerSecond, final int minimumDelay) {
    delayThread = new Thread(new Runnable() {

      private List<DateTime> sentTimes = new ArrayList<DateTime>(queriesPerSecond);
      private DateTime lastSentTime = new DateTime(0L);

      @Override
      public void run() {
        try {
          while (!delegate.isShutdown()) {
            Runnable r = queue.take();

            long requiredSeparationDelay = lastSentTime.plusMillis(minimumDelay).getMillis()
                - System.currentTimeMillis();
            if (requiredSeparationDelay > 0) {
              Thread.sleep(requiredSeparationDelay);
            }

            DateTime oneSecondAgo = new DateTime().minusSeconds(1);

            // Purge any sent times older than a second
            while (sentTimes.size() > 0 && sentTimes.get(0).compareTo(oneSecondAgo) < 0) {
              sentTimes.remove(0);
            }

            long delay = sentTimes.size() > 0
                ? sentTimes.get(0).plusSeconds(1).getMillis() - System.currentTimeMillis()
                : 0;
            if (sentTimes.size() < queriesPerSecond || delay <= 0) {
              delegate.execute(r);
              lastSentTime = new DateTime();
              sentTimes.add(lastSentTime);
            } else {
              queue.add(r);
              Thread.sleep(delay);
            }
          }
        } catch (InterruptedException ie) {
          log.log(Level.INFO, "Interupted", ie);
        }
      }
    });
    delayThread.setDaemon(true);
    delayThread.start();
  }

  private static ThreadFactory threadFactory(final String name, final boolean daemon) {
    return new ThreadFactory() {
      @Override public Thread newThread(Runnable runnable) {
        Thread result = new Thread(runnable, name);
        result.setDaemon(daemon);
        return result;
      }
    };
  }


  @Override
  public void execute(Runnable runnable) {
    queue.add(runnable);
  }

  // Everything below here is straight delegation.

  @Override
  public void shutdown() {
    delegate.shutdown();
  }

  @Override
  public List<Runnable> shutdownNow() {
    return delegate.shutdownNow();
  }

  @Override
  public boolean isShutdown() {
    return delegate.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return delegate.isTerminated();
  }

  @Override
  public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
    return delegate.awaitTermination(l, timeUnit);
  }

  @Override
  public <T> Future<T> submit(Callable<T> tCallable) {
    return delegate.submit(tCallable);
  }

  @Override
  public <T> Future<T> submit(Runnable runnable, T t) {
    return delegate.submit(runnable, t);
  }

  @Override
  public Future<?> submit(Runnable runnable) {
    return delegate.submit(runnable);
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> callables)
      throws InterruptedException {
    return delegate.invokeAll(callables);
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> callables, long l,
      TimeUnit timeUnit) throws InterruptedException {
    return delegate.invokeAll(callables, l, timeUnit);
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> callables) throws InterruptedException,
  ExecutionException {
    return delegate.invokeAny(callables);
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> callables, long l, TimeUnit timeUnit)
      throws InterruptedException, ExecutionException, TimeoutException {
    return delegate.invokeAny(callables, l, timeUnit);
  }

}