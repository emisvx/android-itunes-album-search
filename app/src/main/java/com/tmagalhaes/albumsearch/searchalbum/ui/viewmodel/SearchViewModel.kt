package com.tmagalhaes.albumfinder.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tmagalhaes.albumsearch.searchalbum.model.Album
import com.tmagalhaes.albumsearch.common.model.Outcome
import com.tmagalhaes.albumsearch.searchalbum.ui.repository.AlbumRepository

class SearchViewModel {

    private lateinit var repository: AlbumRepository

    // Properties

    private val albums: MutableLiveData<List<Album>> by lazy {
        MutableLiveData<List<Album>>().also {
            it.value = emptyList()
        }
    }

    private val loading: MutableLiveData<Boolean> = MutableLiveData()

    private val requestErrorMessage: MutableLiveData<String?> by lazy {
        MutableLiveData<String?>().also {
            it.value = null
        }
    }

    // Interactions

    fun queryAlbums(query: String) {
        loading.value = true
        repository.getAlbums(query) { outcome ->
            loading.value = false
            when (outcome) {
                is Outcome.Success -> {
                    albums.value = outcome.value.results
                    requestErrorMessage.value = null
                }
                is Outcome.Error -> {
                    albums.value = emptyList()
                    requestErrorMessage.value = outcome.message
                }
            }
        }
    }

    fun didShowError() {
        requestErrorMessage.value = null
    }

    /* Accessors
    * We do that in order to not expose MutableLiveData properties since the activity should not change it's value.
    * This can be considered verbose or not. It all depends on how the team decides to proceed. For the sake of
    * completeness I'll add this.
    */
    fun getAlbums() = albums as LiveData<List<Album>>
    fun isLoading() = loading as LiveData<Boolean>
    fun getRequestErrorMessage() = requestErrorMessage as LiveData<String?>

    var someLoad: MutableLiveData<Boolean> = MutableLiveData()

}