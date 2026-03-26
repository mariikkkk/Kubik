package com.example.kubik.presentation.queues

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.kubik.domain.models.QueueItem
import com.example.kubik.domain.models.QueueSlot

class QueuesViewModel: ViewModel() {

    private val _queueList = mutableStateListOf(
        QueueItem(
            1,
            "ТРПП 2",
            "Сегодня дедлайн!",
            26,
            5,
            "Открыто"
        ),
        QueueItem(
            2,
            "Геймдизайн 2",
            "Перед сдачей прикрепите",
            26,
            2,
            "Закрыто"
        ),
        QueueItem(
            3,
            "ТРПП 1",
            "На первой паре можно",
            26,
            null,
            "Закрыто"
        ),
        QueueItem(
            4,
            "Первая беседа",
            "Файлы уже загружены",
            26,
            15,
            "Открыто"
        )
    )

    val queueList: List<QueueItem> = _queueList
    private val _slots =
        mutableStateListOf<QueueSlot>().apply { // внутренняя переменная, которую никто не может поменять
            repeat(26) { i ->
                add(QueueSlot(id = i + 1))
            }
        }
    val slots: List<QueueSlot> =
        _slots // Экран видит только эту переменную. Все изменения должны проходить через функции

    fun toggleSlot(slotId: Int, queueId: Int) {                            // вызывается при клике по ячейке
        val index = _slots.indexOfFirst { it.id == slotId }    // пробегается по списку и ищет id
        if (index != -1) {                                       // если нашли айди
            val currentSlot = _slots[index]                     // сохраняем состояние
            when {
                // Если это наш слот, то освобождаем его
                currentSlot.isMySlot -> {
                    _slots[index] = currentSlot.copy(studentName = null, isMySlot = false)
                    updateQueueItem(queueId, null)
                }
                // Если место пустое, то занимаем его
                currentSlot.studentName == null -> {
                    _slots[index] = currentSlot.copy(
                        studentName = "Цой Марат",
                        isMySlot = true
                    )
                    updateQueueItem(queueId, slotId)
                }
                // Если занято, то ничего не происходит
                else -> currentSlot
                // Используем copy, чтобы заменить указанные поля на новые и положить новый объект в массив _slots[index]
            }
        }
    }

    fun updateQueueItem(queueId: Int, newPlace: Int?) {
        val queueIndex = queueList.indexOfFirst { it.id == queueId }
        if (queueIndex != -1) {
            val currentQueue = _queueList[queueIndex]
            _queueList[queueIndex] = currentQueue.copy(myPlace = newPlace)
        }
    }

    fun loadSlotsForQueue(queueId: Int) {
        val newSlots = when (queueId) {
            1 -> {
                (1..26).map {
                    QueueSlot(it, if (it == 5) "Куликов" else null, isMySlot = false)

                }
            }
            2 -> {
                (1..26).map{
                    QueueSlot(it, if (it % 2 == 0) "Бурцев" else null, isMySlot = false)
                }
            }
            else -> {
                (1..26).map {
                    QueueSlot(it, null, isMySlot = false)
                }
            }
        }
        _slots.clear()
        _slots.addAll(newSlots)
        }
}