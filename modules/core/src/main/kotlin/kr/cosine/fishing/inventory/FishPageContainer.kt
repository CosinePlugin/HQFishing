package kr.cosine.fishing.inventory

import kr.hqservice.framework.inventory.container.HQContainer

abstract class FishPageContainer<T>(
    slot: Int,
    title: String,
    isCancelled: Boolean
) : HQContainer(slot, title, isCancelled) {

    abstract var page: Int

    abstract val baseList: List<T>

    val chunkedList get() = baseList.chunked(45)

    val currentPageList get() = chunkedList[page]
}