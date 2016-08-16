package com.sinyuk.yuk.ui.feeds;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.sinyuk.yuk.AppModule;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.api.DribbleApi;
import com.sinyuk.yuk.data.shot.DaggerShotRepositoryComponent;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.data.shot.ShotRepository;
import com.sinyuk.yuk.ui.BaseFragment;
import com.sinyuk.yuk.utils.BetterViewAnimator;
import com.sinyuk.yuk.utils.BlackMagics;
import com.sinyuk.yuk.utils.PrefsUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import rx.Observer;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/7/1.
 */
public class FeedsFragment extends BaseFragment {
    private static final int FIRST_PAGE = 1;
    private static final int PRELOAD_THRESHOLD = 2;

    @Inject
    ShotRepository shotRepository;
    @Inject
    RxSharedPreferences mSharedPreferences;
    @BindView(R.id.layout_list)
    RelativeLayout mListLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    SmoothProgressBar smoothProgressBar;
    @BindView(R.id.layout_error)
    RelativeLayout mLayoutError;
    @BindView(R.id.layout_empty)
    RelativeLayout mLayoutEmpty;
    @BindView(R.id.view_animator)
    BetterViewAnimator mViewAnimator;
    private FeedsAdapter mAdapter;


    private int mPage = FIRST_PAGE;
    private String mType = DribbleApi.ALL;
    private boolean isLoading;

    public FeedsFragment() {
        // need a default constructor
    }

    @Override
    protected void beforeInflate() {
        Timber.tag("FeedsFragment");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DaggerShotRepositoryComponent.builder()
                .appModule(new AppModule(activity.getApplication()))
                .build().inject(this);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.feed_list_fragment;
    }

    @Override
    protected void finishInflate() {
        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isLoading) { return; }
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                boolean isBottom =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() >= recyclerView.getAdapter().getItemCount() - PRELOAD_THRESHOLD;
                if (isBottom) { loadFeeds(mPage); }
            }
        });

    }

    private void initData() {
        mAdapter = new FeedsAdapter(mContext, Glide.with(this));

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mViewAnimator.setDisplayedChildId(mAdapter.getDataItemCount() == 0 ? R.id.layout_empty : R.id.layout_list);
            }
        });

        mSharedPreferences.getBoolean(PrefsUtils.auto_play_gif, false)
                .asObservable()
                .subscribe(autoPlayGif -> {
                    mAdapter.setAutoPlayGif(autoPlayGif);
                });

        loadFeeds(mPage);//????为什么打印不出log
    }

    /**
     * Start and show loading more progress bar
     */
    private void showLoadingProgress() {
        isLoading = true;
        if (smoothProgressBar == null) {
            // 加载第一页的时候不用显示这个 那么做一个初始化
            final View loadingView = LayoutInflater.from(mContext).inflate(R.layout.feed_layout_list_footer, mRecyclerView, false);
            mAdapter.setFooterView(loadingView);
            smoothProgressBar = (SmoothProgressBar) loadingView.findViewById(R.id.progress_bar);
        } else {
            BlackMagics.scrollUp(smoothProgressBar).withStartAction(() -> {
                smoothProgressBar.setVisibility(View.VISIBLE);
                smoothProgressBar.progressiveStart();
            });
        }
    }


    /**
     * Contrary to the above method
     */
    private void hideLoadingProgress() {
        isLoading = false;
        if (mPage == FIRST_PAGE) { return; } // 当加载第一页时 什么都不做
        BlackMagics.scrollDown(smoothProgressBar).withEndAction(() -> {
            smoothProgressBar.setVisibility(View.GONE);
            smoothProgressBar.progressiveStop();
        });
    }

    /**
     * 加载错误时
     *
     * @param throwable
     */
    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
        mViewAnimator.setDisplayedChildId(R.id.layout_error);
        mPage = FIRST_PAGE;
    }


    //    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setFilterType(String type) {
        loadFeeds(FIRST_PAGE);
    }

    private final Observer<List<Shot>> addFeedsToList = new Observer<List<Shot>>() {
        @Override
        public void onCompleted() {
            mPage = mPage + 1;
        }

        @Override
        public void onError(Throwable e) {
            handleError(e);
        }

        @Override
        public void onNext(List<Shot> shots) {
            mAdapter.setDataSet(shots);
            Timber.d("Data in adapter %s", shots.toString());
        }
    };

    private void loadFeeds(int page) {
        addSubscription(shotRepository.getShots(mType, page)
                .doOnSubscribe(this::showLoadingProgress)
                .doAfterTerminate(this::hideLoadingProgress)
                .subscribe(addFeedsToList));
    }


}
