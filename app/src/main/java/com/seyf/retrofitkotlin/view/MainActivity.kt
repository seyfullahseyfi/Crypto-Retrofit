package com.seyf.retrofitkotlin.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.seyf.retrofitkotlin.adapter.RecyclerAdapter
import com.seyf.retrofitkotlin.databinding.ActivityMainBinding
import com.seyf.retrofitkotlin.model.CryptoModel
import com.seyf.retrofitkotlin.service.CryptoAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), RecyclerAdapter.Listener{

    private lateinit var binding: ActivityMainBinding
    private val BASE_URL= "https://raw.githubusercontent.com/"
    private var cryptoModels: ArrayList<CryptoModel>? = null
    private var recyclerAdapter: RecyclerAdapter?=null

    //Disposable
    private var compositeDisposable: CompositeDisposable?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)

        //https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json

        compositeDisposable = CompositeDisposable()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        loadData()
    }

    private fun loadData() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CryptoAPI::class.java)


        compositeDisposable?.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))



        /*
        val service = retrofit.create(CryptoAPI::class.java)

        val call = service.getData()

        call.enqueue(object: Callback<List<CryptoModel>>{
            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                    if (response.isSuccessful){
                        response.body()?.let {
                            cryptoModels = ArrayList(it)

                            cryptoModels?.let {
                                recyclerAdapter = RecyclerAdapter(it,this@MainActivity)
                                binding.recyclerView.adapter = recyclerAdapter
                            }
                        }
                    }
            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })
         */
    }

    private fun handleResponse(cryptoList: List<CryptoModel>) {
        cryptoModels = ArrayList(cryptoList)

        cryptoModels?.let {
            recyclerAdapter = RecyclerAdapter(it,this@MainActivity)
            binding.recyclerView.adapter = recyclerAdapter
        }
    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this,"Tıklandı: ${cryptoModel.currency}",Toast.LENGTH_LONG ).show()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }
}