package de.starkling.newsapp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.starkling.adapters.HeadlineAdapter
import de.starkling.newsapp.base.BaseFragment
import de.starkling.newsapp.extensions.showSnackBar
import de.starkling.newsapp.injections.ViewModelFactory
import de.starkling.newsapp.models.Article
import de.starkling.newsapp.rest.response.Status
import de.starkling.newsapp.viewmodels.HomeViewModel
import de.starkling.newsapp_android.R
import de.starkling.selectit.base.OnItemSelectListener
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


class HomeFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: HomeViewModel

    private lateinit var headlineAdapter: HeadlineAdapter

   private val args: HomeFragmentArgs by navArgs()

    override fun inject() {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java]
        viewModel.currentCategory = args.category
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        context?.let {

            headlineAdapter = HeadlineAdapter(it)

            recyclerView.layoutManager = LinearLayoutManager(it)

            recyclerView.adapter = headlineAdapter
            recyclerView.addItemDecoration(DividerItemDecoration(it,LinearLayoutManager.VERTICAL))
            headlineAdapter.addListener(object : OnItemSelectListener<Article> {
                override fun onItemSelected(item: Article, position: Int, view: View) {

                    val extras = FragmentNavigatorExtras(
                        view to "imageView"
                    )
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(item),extras)
                }
            })
        }

        loadNews()

        refreshLayout.setOnRefreshListener {
            loadNews()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> {
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }
        }
        return true
    }
    private fun loadNews() {

        viewModel.getHeadline()

        viewModel.newsHeadlineResponse.observe(this, Observer {

            when (it.status) {
                Status.SUCCESS -> {
                    headlineAdapter.setItems(it.data)
                    refreshLayout.isRefreshing = false
                }
                Status.LOADING -> refreshLayout.isRefreshing = true
            }
        })

        viewModel.newsHeadlineError.observe(this, Observer {
            showSnackBar(it)
        })
    }

}
