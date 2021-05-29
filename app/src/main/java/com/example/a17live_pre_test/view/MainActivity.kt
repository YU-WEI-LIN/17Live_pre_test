package com.example.a17live_pre_test.view


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a17live_pre_test.R
import com.example.a17live_pre_test.view.adapter.UserAdapter
import com.example.a17live_pre_test.model.data.User
import com.example.a17live_pre_test.viewmodel.GithubViewModelFactory
import com.example.a17live_pre_test.viewmodel.UserViewModel


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUserViewModel: UserViewModel
    private lateinit var mGithubViewModelFactory: GithubViewModelFactory
    private lateinit var mSearchButton: Button
    private lateinit var mSearchKeyWordInput: EditText
    private lateinit var mUserRecyclerView: RecyclerView
    private lateinit var mProgressBar: ProgressBar
    //private lateinit var mLinearLayoutManager: LinearLayoutManager
    // initialise loading state
    var mIsLoading = false
    var mIsLastPage = false
    var mIsLoadingFail = false
    var mpageSize = 30
    var mCurrentPage = 1
    val madapter = UserAdapter(this)
    var mTotalPages = 0
    val mLinearLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.initView()
        this.resizeViews()
        this.initViewModel()

    }

    private fun resizeViews() {
        var searchKeyWordLayoutParam = mSearchKeyWordInput.layoutParams
        var editTextWidth = this.resources.displayMetrics.widthPixels * 0.7
        searchKeyWordLayoutParam.width = editTextWidth.toInt()
    }

    private fun initView() {
        mSearchButton = findViewById(R.id.searchButton)
        mSearchKeyWordInput = findViewById(R.id.searchKeyWordInput)
        mUserRecyclerView = findViewById(R.id.repoRecyclerView)
        mProgressBar = findViewById(R.id.progressBar)
        mSearchButton.setOnClickListener(this)
        mUserRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // number of visible items
                var visibleItemCount = mLinearLayoutManager.getChildCount()
                // number of items in layout
                var totalItemCount = mLinearLayoutManager.getItemCount()
                // the position of first visible item
                var firstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition()

                var isNotLoadingAndNotLastPage = !mIsLoading && !mIsLastPage
                // flag if number of visible items is at the last
                var isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
                // validate non negative values
                var isValidFirstItem = firstVisibleItemPosition >= 0
                // validate total items are more than possible visible items
                var totalIsMoreThanVisible = totalItemCount >= mpageSize
                // flag to know whether to load more
                var shouldLoadMore = isValidFirstItem && isAtLastItem && totalIsMoreThanVisible && isNotLoadingAndNotLastPage

                if (shouldLoadMore) {
                    // change loading state
                    mIsLoading = true
                    if (!mIsLoadingFail)
                        mCurrentPage++

                    // mocking network delay for API call
                    Handler().postDelayed(object : Runnable {
                        override fun run() {
                            handleSearchButtonClick()
                        }
                    }, 1000)

                }

            }
        })
    }

    private fun initViewModel() {
        mGithubViewModelFactory = GithubViewModelFactory()
        this.mUserViewModel = ViewModelProviders.of(this, mGithubViewModelFactory).get(UserViewModel::class.java)
        this.mUserViewModel.getUsers().observe(
                this,
                Observer { data -> this.onDataChange(data as MutableList<User>) })
        this.mUserViewModel.isLoadingData().observe(this, Observer { isLoading ->
            this.onLoadingStatusChange(
                    isLoading as Boolean
            )
        })
    }

    private fun setupAdapter(data: MutableList<User>) {
        madapter.setDataset(data)
        mUserRecyclerView.adapter = madapter
        mUserRecyclerView.layoutManager = mLinearLayoutManager

    }

    private fun addAdapter(data: MutableList<User>) {
        madapter.addDataset(data)
    }

    private fun onDataChange(data: MutableList<User>) {

        if (data.isEmpty()) {
            Toast.makeText(this, "API access limit !! Please Try later...", Toast.LENGTH_SHORT).show()
            mIsLoading = false
            mIsLoadingFail = true
            return
        }
        if (mIsLoading) {
            this.addAdapter(data)
            if (this.mUserViewModel.getUserTotalCount() >= 1000)
                mTotalPages = 34
            else
                mTotalPages = this.mUserViewModel.getUserTotalCount() / 30 + if (this.mUserViewModel.getUserTotalCount() % 30 > 0) 1 else 0
            mIsLoading = false
            mIsLoadingFail = false
            mIsLastPage = mCurrentPage == mTotalPages
            if (mIsLastPage)
                Toast.makeText(this, "This is the last page.", Toast.LENGTH_SHORT).show()
        }
        else
            this.setupAdapter(data)
    }

    private fun onLoadingStatusChange(isLoading: Boolean) {
        if (isLoading) {
            this.mProgressBar.visibility = View.VISIBLE
        } else {
            this.mProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun handleSearchButtonClick() {
        var queryString = this.mSearchKeyWordInput.text.toString()
        if (queryString.isNotEmpty()) {
            val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                    currentFocus?.windowToken,
                    InputMethodManager.SHOW_FORCED
            )
            this.mUserViewModel.searchUsers(queryString, mCurrentPage, mpageSize)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.searchButton) {
            mCurrentPage = 1
            mIsLastPage = false
            this.handleSearchButtonClick()
        }
    }
}
