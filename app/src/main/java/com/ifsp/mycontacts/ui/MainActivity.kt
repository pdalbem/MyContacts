package com.ifsp.mycontacts.ui

import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ifsp.mycontacts.R
import com.ifsp.mycontacts.adapter.ContactsAdapter
import com.ifsp.mycontacts.databinding.ActivityMainBinding
import com.ifsp.mycontacts.ui.addFragment.AddContactFragment
import com.ifsp.mycontacts.ui.deleteAll.DeleteAllFragment
import com.ifsp.mycontacts.utils.Constants.BUNDLE_ID
import com.ifsp.mycontacts.utils.DataStatus
import com.ifsp.mycontacts.utils.isVisible
import com.ifsp.mycontacts.viewmodel.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var contactsAdapter: ContactsAdapter

    private val viewModel : DatabaseViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    var selectedItem = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            setSupportActionBar(toolbar)

            btnShowDialog.setOnClickListener {
                AddContactFragment().show(supportFragmentManager,AddContactFragment().tag)
            }

            viewModel.getAllContacts()
            viewModel.contactsList.observe(this@MainActivity){
                when(it.status){
                    DataStatus.Status.LOADING -> {
                        loading.isVisible(true,rvContacts)
                        emptyBody.isVisible(false,rvContacts)

                    }
                    DataStatus.Status.SUCCESS -> {
                        it.isEmpty?.let {
                            showEmpty(it)
                        }
                        loading.isVisible(false,rvContacts)
                        contactsAdapter.differ.submitList(it.data)
                        rvContacts.apply {
                            layoutManager=LinearLayoutManager(this@MainActivity)
                            adapter=contactsAdapter
                        }
                    }
                    DataStatus.Status.ERROR -> {
                        loading.isVisible(false,rvContacts)
                        Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                    }
                }


            }

            toolbar.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.actionDeleteAll -> {
                        DeleteAllFragment().show(supportFragmentManager,DeleteAllFragment().tag)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.actionSort -> {
                        filter()
                        return@setOnMenuItemClickListener true
                    }
                    else -> {return@setOnMenuItemClickListener false}
                }
            }

            val swipCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                   val position=viewHolder.adapterPosition
                    val contact = contactsAdapter.differ.currentList[position]
                    when(direction){
                        ItemTouchHelper.LEFT -> {
                           viewModel.deleteContact(contact)
                           Snackbar.make(binding.root,"Item Deleted!",Snackbar.LENGTH_LONG).apply {
                               setAction("UNDO"){
                                   viewModel.saveContacts(false,contact)
                               }
                           }.show()
                        }
                        ItemTouchHelper.RIGHT -> {
                            val addContactFragment=AddContactFragment()
                            val bundle = Bundle()
                            bundle.putInt(BUNDLE_ID,contact.id)
                            addContactFragment.arguments=bundle
                            addContactFragment.show(supportFragmentManager,AddContactFragment().tag)
                        }
                    }

                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {

                    RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                        .addSwipeLeftLabel("Delete")
                        .addSwipeLeftBackgroundColor(Color.RED)
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .setSwipeLeftLabelColor(Color.WHITE)
                        .setSwipeLeftActionIconTint(Color.WHITE)
                        .addSwipeRightLabel("Edit")
                        .addSwipeRightBackgroundColor(Color.GREEN)
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                        .setSwipeRightLabelColor(Color.WHITE)
                        .setSwipeRightActionIconTint(Color.WHITE)
                        .create()
                        .decorate()

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }

            }

            val itemTouchHelper=ItemTouchHelper(swipCallback)
            itemTouchHelper.attachToRecyclerView(rvContacts)

        }


    }

    private fun showEmpty(isShown: Boolean) {
        binding.apply {
            if (isShown) {
                emptyBody.visibility = View.VISIBLE
                listBody.visibility = View.GONE
            } else {
                emptyBody.visibility = View.GONE
                listBody.visibility = View.VISIBLE
            }
        }
    }


    private fun filter(){

        val builder = AlertDialog.Builder(this)
        val sortItem = arrayOf("Newer(Default)","Name : A-Z" , "Name : Z-A")
        builder.setSingleChoiceItems(sortItem,selectedItem){ dialog , item ->
            when(item){
                0-> viewModel.getAllContacts()
                1->viewModel.sortedASC()
                2->viewModel.sortedDESC()
            }
            selectedItem=item
            dialog.dismiss()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val search = menu!!.findItem(R.id.actionSearch)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.getSearchContacts(newText!!)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

}