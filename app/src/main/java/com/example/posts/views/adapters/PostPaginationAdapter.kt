package com.example.posts.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.posts.R
import com.example.posts.data.model.Data
import com.example.posts.databinding.ItemLoadingBinding
import com.example.posts.databinding.ItemPostsBinding
import com.example.posts.views.`interface`.PaginationAdapterCallback
import com.example.posts.views.activities.HomeActivity

class PostPaginationAdapter(private var mActivity: HomeActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() ,
    PaginationAdapterCallback {

    private val item: Int = 0
    private val loading: Int = 1

    private var isLoadingAdded: Boolean = false
    private var retryPageLoad: Boolean = false

    private var errorMsg: String? = ""

    private var postsModels: MutableList<Data> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == item) {
            val binding: ItemPostsBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_posts,
                parent,
                false
            )
            PostsVH(binding)
        } else {
            val binding: ItemLoadingBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_loading,
                parent,
                false
            )
            LoadingVH(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = postsModels[position]
        if (getItemViewType(position) == item) {
            val myOrderVH: PostsVH = holder as PostsVH
            myOrderVH.itemRowBinding.postTitle.text = model.title
            myOrderVH.itemRowBinding.postBody.text = model.body
            myOrderVH.bind(model)
        } else {
            val loadingVH: LoadingVH = holder as LoadingVH
            if (retryPageLoad) {
                loadingVH.itemRowBinding.loadmoreErrorlayout.visibility = View.VISIBLE
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.GONE

                if (errorMsg != null) loadingVH.itemRowBinding.loadmoreErrortxt.text = errorMsg
                else loadingVH.itemRowBinding.loadmoreErrortxt.text =
                    mActivity.getString(R.string.error_msg_unknown)

            } else {
                loadingVH.itemRowBinding.loadmoreErrorlayout.visibility = View.GONE
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.VISIBLE
            }

            loadingVH.itemRowBinding.loadmoreErrorlayout.setOnClickListener {
                showRetry(false, "")
                retryPageLoad()
            }
        }
    }

    override fun getItemCount(): Int {
        return if (postsModels.size > 0) postsModels.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            item
        } else {
            if (position == postsModels.size - 1 && isLoadingAdded) {
                loading
            } else {
                item
            }
        }
    }

    override fun retryPageLoad() {
        mActivity.loadNextPage()
    }


    class PostsVH(binding: ItemPostsBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemPostsBinding = binding
        fun bind(obj: Any?) {
//            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
        }
    }

    class LoadingVH(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemLoadingBinding = binding
    }

    fun showRetry(show: Boolean, errorMsg: String) {
        retryPageLoad = show
        notifyItemChanged(postsModels.size - 1)
        this.errorMsg = errorMsg
    }

    fun addAll(posts: MutableList<Data>) {
        for (post in posts) {
            add(post)
        }
    }

    fun add(post: Data) {
        postsModels.add(post)
        notifyItemInserted(postsModels.size - 1)
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Data())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position: Int = postsModels.size - 1
        val post: Data = postsModels[position]

        if (post != null) {
            postsModels.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}