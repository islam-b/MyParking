package com.example.myparking

import android.provider.CallLog
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.myparking.models.FilterParkingsModel
import com.example.myparking.models.Parking
import com.example.myparking.repositories.ParkingListRepository
import com.example.myparking.services.ParkingService
import com.example.myparking.utils.InjectorUtils
import com.example.myparking.viewmodels.ParkingListViewModel
import com.nhaarman.mockito_kotlin.doReturn

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio


import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import org.bouncycastle.crypto.tls.ConnectionEnd.server


/**
 * Parking list view model tests
 */
class ParkingListViewModelUnitTest {
    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    private val mockFilterState: MutableLiveData<FilterParkingsModel> = mock()
    private lateinit var viewModel: ParkingListViewModel

    @Before
    fun init() {
        viewModel = createViewModel(
            parkingListRepository = mock {
                on {
                    getParkingsList(
                        mockFilterState,
                        FAKE_AUTOMOBILISTE_ID,
                        FAKE_GPS_START,
                        FAKE_GPS_DESTINATION
                    )
                } doReturn MutableLiveData(fakeParkingList)
            }
        )
        viewModel.mParkingList = MutableLiveData(fakeParkingList)
    }
    @Test
    fun `given repository emits value when start view model then contains that value`() {
        assertEquals(viewModel.mParkingList.value!![0].idParking, FAKE_PARKING_ID)
    }
    @Test
    fun `parking list sorted by distance`() {
        var distance1 = viewModel.mParkingList.value!![0].routeInfo?.travelDistance!!
        var distance2 = viewModel.mParkingList.value!![1].routeInfo?.travelDistance!!
        assert(distance1 > distance2)
        viewModel.sortByDistance()
        distance1 = viewModel.mParkingList.value!![0].routeInfo?.travelDistance!!
        distance2 = viewModel.mParkingList.value!![1].routeInfo?.travelDistance!!
        assert(distance1 < distance2)
    }

    @Test
    fun `parking list sorted by travel time`() {
        var travel1 = viewModel.mParkingList.value!![0].routeInfo?.travelTime!!
        var travel2 = viewModel.mParkingList.value!![1].routeInfo?.travelTime!!
        assert(travel1 > travel2)
        viewModel.sortByDistance()
        travel1 = viewModel.mParkingList.value!![0].routeInfo?.travelDistance!!
        travel2 = viewModel.mParkingList.value!![1].routeInfo?.travelDistance!!
        assert(travel1 < travel2)
    }

    private fun createViewModel(
        parkingListRepository: ParkingListRepository = mock(),
        idAutomobiliste: Int = FAKE_AUTOMOBILISTE_ID,
        start: String? = FAKE_GPS_START,
        destination: String? = FAKE_GPS_DESTINATION

    ) = ParkingListViewModel(
        parkingListRepository = parkingListRepository,
        idAutomobiliste = idAutomobiliste,
        start = start,
        destination = destination
    )

    companion object {
        const val FAKE_AUTOMOBILISTE_ID = 1
        const val FAKE_PARKING_ID = 2
        const val FAKE_GPS_START = "36.705039,3.173912"
        const val FAKE_GPS_DESTINATION = "36.7275239,3.0448747"
        val fakeParkingList = TestUtils.getParkingListMock()
    }
}

interface SimulatedCallback {
    fun onSuccess(result: List<String>)
    fun onFail(code: Int)
}

/**
 * Parking list repository tests
 */

class ParkingListRepositoryUnitTest {
    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var parkingService: ParkingService
    lateinit var parkingListRepository: ParkingListRepository
    lateinit var mockWebServer: MockWebServer
    private val mock = mock<InjectorUtils>()

    // mocks webserver and parking service
    @Before
    fun init() {
        mockWebServer = MockWebServer()
        //mock service
        parkingService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/parking/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ParkingService::class.java)
        MockitoAnnotations.initMocks(this)
        this.parkingListRepository = ParkingListRepository()

        this.parkingListRepository.service = parkingService

        whenever(mock.provideParkingService()).thenReturn(this.parkingService)
    }

    @Test
    fun `fetchs parking list`() {
        //mock the response on mocked service called
        enqueueResponse("GetParkingResponseMock.json")
        val filterParkingsModel = MutableLiveData<FilterParkingsModel>()
        filterParkingsModel.value = FilterParkingsModel(1,1,"1,2",1,1)
        //invoke
        this.parkingListRepository.getParkingsList(filterParkingsModel, FAKE_AUTOMOBILISTE_ID, FAKE_GPS_START, FAKE_GPS_DESTINATION)
        val request = mockWebServer.takeRequest()

        //TEST
        assertEquals(request.path, "/parking/")
        assertNotNull(this.parkingListRepository.getParkings())
        assertEquals(this.parkingListRepository.getParkings(), 2)
    }
    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
//        val inputStream = javaClass.classLoader!!
//            .getResourceAsStream(fileName)
//        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
//        for ((key, value) in headers) {
//            mockResponse.addHeader(key, value)
//        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(
                    "[{\"idParking\":2,\"nbEtages\":2,\"nbPlaces\":20,\"nbPlacesLibres\":6,\"nom\":\"Parking Said hamdine\",\"adresse\":\"Alger\",\"imageUrl\":\"https://www.miresparis.com/wp-content/uploads/2018/06/C1577-bis-241.jpg\",\"lattitude\":36.7275239,\"longitude\":3.0448747,\"etages\":[{\"idEtage\":2,\"nbPlaces\":10}],\"tarifs\":[{\"idTarif\":2,\"duree\":60.0,\"prix\":120.0}],\"termes\":[{\"idTerme\":3,\"contenu\":\"TERME1\"},{\"idTerme\":4,\"contenu\":\"TERM2\"}],\"paiments\":[{\"idPaiment\":1,\"type\":\"CIB\",\"iconUrl\":\"https://cdn1.iconfinder.com/data/icons/ios-11-glyphs/30/money_bag-512.png\"},{\"idPaiment\":2,\"type\":\"Edahbiya\",\"iconUrl\":\"https://cdn1.iconfinder.com/data/icons/ios-11-glyphs/30/money_bag-512.png\"}],\"equipements\":[{\"idEquipement\":1,\"designation\":\"Surveillance\",\"iconUrl\":\"https://image.flaticon.com/icons/png/512/68/68544.png\"},{\"idEquipement\":2,\"designation\":\"H24\",\"iconUrl\":\"https://image.flaticon.com/icons/png/512/68/68544.png\"}],\"horairesStatus\":[{\"days\":[\"Dimanche\"],\"HeureOuverture\":\"07:00:00\",\"HeureFermeture\":\"19:00:00\"}],\"ouvert\":\"Fermé\",\"routeInfo\":{\"travelDistance\":0,\"travelTime\":0,\"walkingDistance\":0,\"walkingTime\":0,\"canWalk\":true}}]"
                )
        )

    }

    companion object {
        const val FAKE_AUTOMOBILISTE_ID = 1
        const val FAKE_PARKING_ID = 2
        const val FAKE_GPS_START = "36.705039,3.173912"
        const val FAKE_GPS_DESTINATION = "36.7275239,3.0448747"
        val fakeParkingList = TestUtils.getParkingListMock()
    }
}
