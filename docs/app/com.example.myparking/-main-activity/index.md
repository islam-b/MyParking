[app](../../index.md) / [com.example.myparking](../index.md) / [MainActivity](./index.md)

# MainActivity

`class MainActivity : AppCompatActivity, OnNavigationItemSelectedListener, SpaceOnClickListener`

Main activity
This activity contains two view: Map view and List view*

**Author**
BOUAYACHE

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Main activity This activity contains two view: Map view and List view*`MainActivity()` |

### Properties

| Name | Summary |
|---|---|
| [AUTOCOMPLETE_REQUEST_CODE](-a-u-t-o-c-o-m-p-l-e-t-e_-r-e-q-u-e-s-t_-c-o-d-e.md) | `val AUTOCOMPLETE_REQUEST_CODE: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [networkReceiver](network-receiver.md) | the Broadcast Receiver, it receives the connectivity state`val networkReceiver: `[`NetworkReceiver`](../../com.example.myparking.utils/-network-receiver/index.md) |

### Functions

| Name | Summary |
|---|---|
| [onActivityResult](on-activity-result.md) | Handle the result of an intent request`fun onActivityResult(requestCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, resultCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, data: `[`Intent`](https://developer.android.com/reference/android/content/Intent.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onBackPressed](on-back-pressed.md) | This function allows to return to previous activity`fun onBackPressed(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onCentreButtonClick](on-centre-button-click.md) | This function opens the filter dialog, it is triggered when the user click on the filter button`fun onCentreButtonClick(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onCreate](on-create.md) | This function initialize the main activity:`fun onCreate(savedInstanceState: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onCreateOptionsMenu](on-create-options-menu.md) | This function create the options for the toolbar menu`fun onCreateOptionsMenu(menu: `[`Menu`](https://developer.android.com/reference/android/view/Menu.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [onItemClick](on-item-click.md) | This function opens one of the views (list or map) depending on the clicked item`fun onItemClick(itemIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, itemName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onItemReselected](on-item-reselected.md) | `fun onItemReselected(itemIndex: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, itemName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onNavigationItemSelected](on-navigation-item-selected.md) | This function manage the drawer layout navigation, it perform actions according to selected items from the drawer navigation`fun onNavigationItemSelected(item: `[`MenuItem`](https://developer.android.com/reference/android/view/MenuItem.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [onOptionsItemSelected](on-options-item-selected.md) | This function manage menu items actions, it perform actions according to selected items from the toolbar menu`fun onOptionsItemSelected(item: `[`MenuItem`](https://developer.android.com/reference/android/view/MenuItem.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [startAutoCompleteIntent](start-auto-complete-intent.md) | Request an intent to show the search bar from googla maps SDK`fun startAutoCompleteIntent(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Functions

| Name | Summary |
|---|---|
| [newIntent](new-intent.md) | Create new intent to start the main activity`fun newIntent(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`Intent`](https://developer.android.com/reference/android/content/Intent.html) |
