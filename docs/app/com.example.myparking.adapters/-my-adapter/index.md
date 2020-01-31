[app](../../index.md) / [com.example.myparking.adapters](../index.md) / [MyAdapter](./index.md)

# MyAdapter

`abstract class MyAdapter<ItemClass, ItemBinding> : Adapter<MyViewHolder<ItemClass, ItemBinding>>`

### Types

| Name | Summary |
|---|---|
| [ItemAdapterListener](-item-adapter-listener/index.md) | `interface ItemAdapterListener<ItemClass>` |
| [MyViewHolder](-my-view-holder/index.md) | `inner class MyViewHolder<ItemClass, ItemBinding> : ViewHolder` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `MyAdapter(itemList: `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<ItemClass>, itemRowLayout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, listener: ItemAdapterListener<ItemClass>)` |

### Functions

| Name | Summary |
|---|---|
| [getItemCount](get-item-count.md) | `open fun getItemCount(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [onCreateViewHolder](on-create-view-holder.md) | `open fun onCreateViewHolder(parent: `[`ViewGroup`](https://developer.android.com/reference/android/view/ViewGroup.html)`, viewType: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): MyViewHolder<ItemClass, ItemBinding>` |

### Inheritors

| Name | Summary |
|---|---|
| [DurationAdapter](../-duration-adapter/index.md) | `class DurationAdapter : `[`MyAdapter`](./index.md)`<`[`Duration`](../../com.example.myparking.models/-duration/index.md)`, <ERROR CLASS>>` |
| [EquipementAdapter](../-equipement-adapter/index.md) | `class EquipementAdapter : `[`MyAdapter`](./index.md)`<`[`Equipement`](../../com.example.myparking.models/-equipement/index.md)`, <ERROR CLASS>>` |
| [FavoriteParkingAdapter](../-favorite-parking-adapter/index.md) | `class FavoriteParkingAdapter : `[`MyAdapter`](./index.md)`<`[`Parking`](../../com.example.myparking.models/-parking/index.md)`, <ERROR CLASS>>` |
| [HoraireAdapter](../-horaire-adapter/index.md) | `class HoraireAdapter : `[`MyAdapter`](./index.md)`<`[`Horaire`](../../com.example.myparking.models/-horaire/index.md)`, <ERROR CLASS>>` |
| [PaiementAdapter](../-paiement-adapter/index.md) | `class PaiementAdapter : `[`MyAdapter`](./index.md)`<`[`Paiement`](../../com.example.myparking.models/-paiement/index.md)`, <ERROR CLASS>>` |
| [ParkingCarouselAdapter](../-parking-carousel-adapter/index.md) | `class ParkingCarouselAdapter : `[`MyAdapter`](./index.md)`<`[`ParkingModel`](../../com.example.myparking.models/-parking-model/index.md)`, <ERROR CLASS>>` |
| [ParkingsListAdapter](../-parkings-list-adapter/index.md) | `class ParkingsListAdapter : `[`MyAdapter`](./index.md)`<`[`Parking`](../../com.example.myparking.models/-parking/index.md)`, <ERROR CLASS>>` |
| [ReservationAdapter](../-reservation-adapter/index.md) | `class ReservationAdapter : `[`MyAdapter`](./index.md)`<`[`Reservation`](../../com.example.myparking.models/-reservation/index.md)`, <ERROR CLASS>>` |
| [ServiceAdapter](../-service-adapter/index.md) | `class ServiceAdapter : `[`MyAdapter`](./index.md)`<`[`Service`](../../com.example.myparking.models/-service/index.md)`, <ERROR CLASS>>` |
| [TarifsAdapter](../-tarifs-adapter/index.md) | `class TarifsAdapter : `[`MyAdapter`](./index.md)`<`[`Tarif`](../../com.example.myparking.models/-tarif/index.md)`, <ERROR CLASS>>` |
