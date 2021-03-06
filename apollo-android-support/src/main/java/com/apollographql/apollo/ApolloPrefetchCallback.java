package com.apollographql.apollo;

import android.os.Handler;

import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.exception.ApolloHttpException;
import com.apollographql.apollo.exception.ApolloNetworkException;

import org.jetbrains.annotations.NotNull;

import static com.apollographql.apollo.api.internal.Utils.checkNotNull;

/**
 * Android wrapper for {@link ApolloPrefetch.Callback} to be operated on specified {@link Handler}
 */
public final class ApolloPrefetchCallback extends ApolloPrefetch.Callback {
  final ApolloPrefetch.Callback delegate;
  private final Handler handler;

  /**
   * Wraps {@code callback} to be be operated on specified {@code handler}
   *
   * @param callback original callback to delegates calls
   * @param handler  the callback will be run on the thread to which this handler is attached
   */
  public static <T> ApolloPrefetchCallback wrap(@NotNull ApolloPrefetch.Callback callback, @NotNull Handler handler) {
    return new ApolloPrefetchCallback(callback, handler);
  }

  /**
   * @param callback original callback to delegates calls
   * @param handler  the callback will be run on the thread to which this handler is attached
   */
  public ApolloPrefetchCallback(@NotNull ApolloPrefetch.Callback callback, @NotNull Handler handler) {
    this.delegate = checkNotNull(callback, "callback == null");
    this.handler = checkNotNull(handler, "handler == null");
  }

  @Override public void onSuccess() {
    handler.post(new Runnable() {
      @Override public void run() {
        delegate.onSuccess();
      }
    });
  }

  @Override public void onFailure(@NotNull final ApolloException e) {
    handler.post(new Runnable() {
      @Override public void run() {
        delegate.onFailure(e);
      }
    });
  }

  @Override public void onHttpError(@NotNull final ApolloHttpException e) {
    handler.post(new Runnable() {
      @Override public void run() {
        delegate.onHttpError(e);
      }
    });
  }

  @Override public void onNetworkError(@NotNull final ApolloNetworkException e) {
    handler.post(new Runnable() {
      @Override public void run() {
        delegate.onNetworkError(e);
      }
    });
  }
}
