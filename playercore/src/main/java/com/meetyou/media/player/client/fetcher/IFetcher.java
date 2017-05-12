package com.meetyou.media.player.client.fetcher;

/**
 * Created by Linhh on 17/1/16.
 */

public interface IFetcher<T> {
    public void fetch(T source) throws Exception;
    void setFetcherListener(AbstractFetcher.FetcherListener fetcherListener);
    String getSource();
}
