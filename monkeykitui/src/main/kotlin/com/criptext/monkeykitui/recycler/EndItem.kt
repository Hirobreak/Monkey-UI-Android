package com.criptext.monkeykitui.recycler

import android.graphics.Bitmap

/**
 * Created by gesuwall on 4/18/16.
 */

class EndItem : MonkeyItem {
    override fun getPlaceholderFilePath(): String {
        throw UnsupportedOperationException()
    }

    override fun getAudioDuration(): Long {
        throw UnsupportedOperationException()
    }

    override fun isIncomingMessage(): Boolean = true

    override fun getDeliveryStatus(): MonkeyItem.DeliveryStatus  = MonkeyItem.DeliveryStatus.read

    override fun getMessageType(): Int = MonkeyItem.MonkeyItemType.MoreMessages.ordinal

    override fun getMessageTimestamp(): Long = 0

    override fun getMessageText(): String {
        throw UnsupportedOperationException()
    }

    override fun getMessageId(): String = "0"

    override fun getFileSize(): Long {
        throw UnsupportedOperationException()
    }

    override fun getFilePath(): String {
        throw UnsupportedOperationException()
    }

    override fun getContactSessionId(): String = ""

}
