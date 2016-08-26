package com.example.hasang.tomas.base.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.hasang.tomas.network.Model;

/**
 * Created by hasang on 16. 1. 27..
 */
public class BaseViewHolder<M> extends RecyclerView.ViewHolder {

    protected OnItemClickListener mOnItemClickListener;

    protected M mModel;

    public BaseViewHolder(View convertView) {
        super(convertView);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(getLayoutPosition(), mModel);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemLongClick(getLayoutPosition(), mModel);
                return true;
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setData(M postData) throws NullPointerException {
        mModel = postData;
    }

    public interface OnItemClickListener<M> {
        void onItemClick(int position, M model);
        void onItemLongClick(int position, M model);
    }
}