package ee.mtiidla.freelane.restapi.viewmodel.mapper

interface ViewModelMapper<T, ViewModel> {

    fun map(item: T): ViewModel

    fun map(item: List<T>): List<ViewModel> = item.map(::map)
}
