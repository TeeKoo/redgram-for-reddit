package com.matie.redgram.managers.presenters;

import android.util.Log;
import android.widget.Toast;

import com.matie.redgram.managers.rxbus.RxBus;
import com.matie.redgram.models.PostItem;
import com.matie.redgram.models.events.SubredditEvent;
import com.matie.redgram.network.api.reddit.RedditClient;
import com.matie.redgram.views.widgets.ApplicationViews.HomeView;
import com.matie.redgram.views.widgets.PostList.PostRecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static rx.android.app.AppObservable.bindFragment;

/**
 * Created by matie on 12/04/15.
 */

public class HomePresenterImpl implements HomePresenter{

    final private HomeView homeView;
    final private PostRecyclerView homeRecyclerView;
    final private RedditClient redditClient;

    static private RxBus rxBus;

    private CompositeSubscription subscriptions;

    private List<PostItem> items;

    private Subscription subredditSubscription;

    /**
     * Called onCreate(View) of Activity/Fragment
     * @param homeView
     */
    public HomePresenterImpl(HomeView homeView) {
        this.homeView = homeView;
        this.homeRecyclerView = homeView.getRecyclerView();
        this.redditClient = RedditClient.getInstance(false);
        this.items = new ArrayList<PostItem>();
    }

    /**
     * Called onStart of Activity/Fragment
     */
    @Override
    public void registerForEvents() {
        rxBus = RxBus.getDefault();
        subscriptions = new CompositeSubscription();
        subscriptions.add(getEventHandler());
        subscriptions.add(subredditSubscription);
    }
    /**
     * Called onStop of Activity/Fragment
     */
    @Override
    public void unregisterForEvents() {
        subscriptions.unsubscribe();
    }

    /**
     * List populated onCreate(View)
     *
     * todo: Check if it's better to populate onCreate or onStart!!!
     */
    @Override
    public void populateView() {
        homeView.showProgress();
        subredditSubscription =
                (Subscription)bindFragment(homeView.getFragment(), redditClient.getSubredditListing("toronto"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostItem>() {
                    @Override
                    public void onCompleted() {
                        homeView.hideProgress();
                        homeRecyclerView.replaceWith(items);
                    }

                    @Override
                    public void onError(Throwable e) {
                        homeView.showErrorMessage();
                    }

                    @Override
                    public void onNext(PostItem postItem) {
                        items.add(postItem);
                        Log.d("ITEM URL", postItem.getAuthor() + "--" + postItem.getType() + "--" + postItem.getDomain());
                    }
                });
    }

    private Subscription getEventHandler(){
        return (Subscription)bindFragment(homeView.getFragment(), rxBus.toObservable())
                .subscribe(event -> identifyAndPerformEvent(event));
    }

    private void identifyAndPerformEvent(Object event){
        if(event instanceof SubredditEvent){
            Toast.makeText(homeView.getContext(), "subscribed YO!", Toast.LENGTH_LONG).show();
        }

    }

}
