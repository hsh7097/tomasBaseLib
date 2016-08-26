package com.example.hasang.tomas.base.recycler;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hasang.tomas.AppContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasang on 16. 1. 27..
 */
public class BaseRecyclerAdapter<M> extends RecyclerView.Adapter<BaseViewHolder> {
    public static final int POSTTYPE_HEADERVIEWHOLDER = 99; //기타
    public static final int POSTTYPE_FOOTERVIEWHOLDER = 98; //기타
    public static final int POSTTYPE_DEFAULTVIEWHOLDER = 97; //기타
    protected BaseViewHolder mHeaderViewHolder = null;
    protected BaseViewHolder mFooterViewHolder = null;

    protected boolean useHeader = false;
    protected boolean useFooter = false;
    protected OnAddDataLstener mOnAddDataLstener;
    protected ArrayList<M> mModelList = new ArrayList<M>();
    protected BaseViewHolder.OnItemClickListener mOnItemClickListener;

    protected String mPagenationId = null;


    public BaseRecyclerAdapter() {
    }

    /**
     * 데이터 입력 (추후 추가되는데이터도 동일시 입력)
     *
     * @param modelArrayList 추가될 데이터
     */
    public void addData(ArrayList<M> modelArrayList) {
        int itemCount = getItemCount();
        mModelList.addAll(modelArrayList);
        notifyDataSetChanged();
//        if (itemCount == (getUseHeader() ? 1 : 0) || modelArrayList.size() == 0) {
//            notifyDataSetChanged();
//        } else {
//            notifyItemRangeChanged(itemCount, modelArrayList.size());
//        }
    }

    /**
     * 뷰타입에 따라 보이는 화면 설정(다른 타입이 있을시 이부분 오버라이딩해서 씀)
     *
     * @param position 요청할 뷰 타입 번호
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        int returntype = 0;
        if (position == 0 && useHeader)
            returntype = POSTTYPE_HEADERVIEWHOLDER;
        else if (position == getItemCount() - 1 - (useHeader ? 1 : 0) && useFooter)
            returntype = POSTTYPE_FOOTERVIEWHOLDER;
        else {
            returntype = POSTTYPE_DEFAULTVIEWHOLDER;
        }
        return returntype;
    }

    /**
     * 아이템 갯수
     *
     * @return
     */
    @Override
    public int getItemCount() {
        int returnCount = mModelList.size();
        if (useHeader)
            returnCount++;
        if (useFooter)
            returnCount++;
        return returnCount;
    }

    /**
     * 화면 생성
     *
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return null;
    }

    /**
     * 데이터 세팅
     * 데이터값이 없을경우 해당 아이템 삭제
     *
     * @param defaultViewHolder
     * @param position          해당 아이템의 포지션
     */
    @Override
    public void onBindViewHolder(BaseViewHolder defaultViewHolder, int position) {

        if (mOnItemClickListener != null) {
            defaultViewHolder.setOnItemClickListener(mOnItemClickListener);
        }
        if (position == 0 && useHeader) {
        } else if (position == getItemCount() - 1 && useFooter) {
        } else {
            try {
                defaultViewHolder.setData(this.getItem(position));
            } catch (NullPointerException e) {
                deleteItem(position - (useHeader ? 1 : 0));
            }
        }
    }

    /**
     * 데이터 없는 아이템 삭제
     *
     * @param itemPosition 삭제될 아이템 번호
     */
    private void deleteItem(final int itemPosition) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mModelList.remove(itemPosition);
                notifyDataSetChanged();
                Toast.makeText(AppContext.getInstance().getContext(), "잘못된 정보로 인해 아이템이 하나지워졌습니다.", Toast.LENGTH_SHORT).show();
            }
        }, 200);
    }

    /**
     * 페이지네이션을 위한 마지막 데이터
     *
     * @return
     */
    protected M getLastItem() {
        if (mModelList == null || mModelList.size() == 0) {
            return null;
        }
        return mModelList.get(mModelList.size() - 1);
    }

    /**
     * 아이템 가져오기
     *
     * @param position 가져올 아이템 위치
     * @return
     */
    public M getItem(int position) {
        if (position != 0)
            position = position - (useHeader ? 1 : 0);
        if (mModelList == null) {
            return null;
        }
        if (mModelList.size() == 0) {
            return null;
        }
        return mModelList.get(position);
    }

    /**
     * 아이템 전체 가져오기
     *
     * @return
     */
    protected ArrayList<M> getModelList() {
        return mModelList;
    }

    /**
     * 헤더뷰 사용
     *
     * @param headerVIewHolder 상단에 보일 헤더뷰
     */
    public void setUseHeader(BaseViewHolder headerVIewHolder) {
        this.mHeaderViewHolder = headerVIewHolder;
        useHeader = true;
    }

    /**
     * 헤더뷰 사용 취소
     */
    public void setUnUseHeader() {
        this.mHeaderViewHolder = null;
        useHeader = false;
    }

    /**
     * 헤더뷰 사용여부 확인
     *
     * @return
     */
    public boolean getUseHeader() {
        return useHeader;
    }

    public void setUnUseFooter() {
        this.mFooterViewHolder = null;
        useFooter = false;
    }


    public boolean getUseFooter() {
        return useFooter;
    }

    public void setUseFooter(BaseViewHolder footerViewHolder) {
        this.mFooterViewHolder = footerViewHolder;
        useFooter = true;
    }

    public void setOnItemClickListener(BaseViewHolder.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnAddDataLstener(OnAddDataLstener onAddDataLstener) {
        this.mOnAddDataLstener = onAddDataLstener;
    }

//    @Override
//    public void changeData(Model changeData, int type) {
//
//    }

    public interface OnAddDataLstener {
        void onAddData(String lastId);
    }

    public void clearAdapter() {
        mPagenationId = "";
        mModelList.clear();
        notifyDataSetChanged();
    }


    public void animateTo(List<M> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<M> newModels) {
        for (int i = mModelList.size() - 1; i >= 0; i--) {
            final M model = mModelList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<M> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final M model = newModels.get(i);
            if (!mModelList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<M> newModels) {
        int toPositionSize = newModels.size() - 1;
        for (int toPosition = toPositionSize; toPosition >= 0; toPosition--) {
            final M model = newModels.get(toPosition);
            final int fromPosition = mModelList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private M removeItem(int position) {
        final M model = mModelList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, M model) {
        if (position > mModelList.size() && mModelList.size() != 0) {
            mModelList.add(mModelList.size() - 1, model);
        } else {
            mModelList.add(position, model);
        }

        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final M model = mModelList.remove(fromPosition);
        if (toPosition > mModelList.size() && mModelList.size() != 0) {
            mModelList.add(mModelList.size() - 1, model);
        } else {
            mModelList.add(toPosition, model);
        }
        notifyItemMoved(fromPosition, toPosition);
    }

}
