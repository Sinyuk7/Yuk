package com.sinyuk.yuk.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sinyuk.yuk.App;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.api.DribbleService;
import com.sinyuk.yuk.data.shot.Attachment;
import com.sinyuk.yuk.ui.BaseFragment;
import com.sinyuk.yuk.utils.lists.GravitySnapHelper;
import com.sinyuk.yuk.utils.lists.SlideInUpAnimator;
import com.sinyuk.yuk.widgets.FourThreeImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sinyuk on 16/9/9.
 */
public class AttachmentFragment extends BaseFragment {
    private static final String KEY_ID = "ID";
    private static final String TAG = "AttachmentFragment";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Inject
    DribbleService dribbleService;
    private long mId;
    private AttachmentAdapter mAdapter;
    private List<Attachment> mAttachmentList = new ArrayList<>();

    public static AttachmentFragment newInstance(long id) {
        Bundle args = new Bundle();
        args.putLong(KEY_ID, id);

        AttachmentFragment fragment = new AttachmentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void beforeInflate() {
        mId = getArguments().getLong(KEY_ID);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.get(context).getAppComponent().inject(this);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.detail_activity_attachment_fragment;
    }

    @Override
    protected void finishInflate() {
        initRecyclerView();
        createPlaceHolder();
        initAdapter();
        loadAttachments();
    }

    private void initAdapter() {
        mAdapter = new AttachmentAdapter();

        mAdapter.setHasStableIds(true);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadAttachments() {
        dribbleService.attachments(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(Observable::from)
                .subscribe(new Observer<Attachment>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Attachment attachment) {
                        mAttachmentList.add(attachment);
                        mAdapter.notifyItemInserted(mAttachmentList.size() - 1);
                    }
                });
    }

    private void initRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        final SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);

        snapHelperStart.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);

        mRecyclerView.setItemAnimator(new SlideInUpAnimator(new FastOutSlowInInterpolator()));

//        mRecyclerView.addItemDecoration(new FeedsItemDecoration(getContext()));
    }

    private void createPlaceHolder() {

    }

    public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {


        @Override
        public AttachmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AttachmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_activity_attachment_item, parent, false));
        }

        @Override
        public void onBindViewHolder(AttachmentViewHolder holder, int position) {
            if (mAttachmentList.get(position) != null) {
                final Attachment data = mAttachmentList.get(position);
                Glide.with(AttachmentFragment.this).load(data.getThumbnailUrl())
                        .error(R.drawable.pic_fill)
                        .placeholder(R.drawable.pic_fill)
                        .crossFade(400)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.mThumbnail);

                holder.mThumbnail.setOnClickListener(view -> {
                    Log.d(TAG, "Click At " + position);
                });
            }
        }


        @Override
        public int getItemCount() {
            return mAttachmentList == null ? 0 : mAttachmentList.size();
        }


        public class AttachmentViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.thumbnail)
            FourThreeImageView mThumbnail;

            public AttachmentViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
