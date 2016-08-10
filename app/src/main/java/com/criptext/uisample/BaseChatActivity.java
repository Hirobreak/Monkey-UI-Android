package com.criptext.uisample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.criptext.monkeykitui.input.BaseInputView;
import com.criptext.monkeykitui.input.listeners.InputListener;
import com.criptext.monkeykitui.recycler.ChatActivity;
import com.criptext.monkeykitui.recycler.MonkeyItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by gesuwall on 8/10/16.
 */
public abstract class BaseChatActivity extends AppCompatActivity implements ChatActivity {

    private Handler handler = new Handler();
    SlowMessageLoader loader;
    FakeFiles fakeFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loader = new SlowMessageLoader(this);
        fakeFiles = new FakeFiles(this);
        fakeFiles.createAudioFile();
        fakeFiles.createImageFile();
    }
    @Override
    public boolean isOnline() {
        return true;
    }

    public InputListener createInputListener(){
        return new InputListener() {
                @Override
                public void onNewItem(@NotNull MonkeyItem item) {
                    MessageItem newItem = new MessageItem("0", item.getMessageId(), item.getOldMessageId(),
                            item.getMessageText(), item.getMessageTimestamp(), item.getMessageTimestampOrder(),
                            item.isIncomingMessage(), MonkeyItem.MonkeyItemType.values()[item.getMessageType()]);

                    newItem.setStatus(MonkeyItem.DeliveryStatus.read);
                    switch (MonkeyItem.MonkeyItemType.values()[item.getMessageType()]) {
                        case audio: //init audio MessageItem
                            newItem.setDuration(item.getAudioDuration());
                            newItem.setMessageContent(item.getFilePath());
                            break;
                        case photo:
                            newItem.setMessageContent(item.getFilePath());
                            break;
                    }

                    smoothlyAddNewItem(newItem); // Add to recyclerView
                }
            };
    }

    private void mockFileNetworkRequests(MonkeyItem item) {
        final MessageItem message = (MessageItem) item;
        Runnable errorCallback = new Runnable() {
            @Override
            public void run() {
                if (message.getDeliveryStatus() == MonkeyItem.DeliveryStatus.sending) {
                    message.setDeliveryStatus(MonkeyItem.DeliveryStatus.error);
                    rebindMonkeyItem(message);
                }
            }
        };

        if (message.getDeliveryStatus() != MonkeyItem.DeliveryStatus.sending) {
            message.setDeliveryStatus(MonkeyItem.DeliveryStatus.sending);
            rebindMonkeyItem(message);
        } else
            handler.postDelayed(errorCallback, 3000);
    }

    abstract void rebindMonkeyItem(MonkeyItem message);

    abstract void addOldMessages(ArrayList<MonkeyItem> messages, boolean hasReachedEnd);

    abstract void smoothlyAddNewItem(MonkeyItem message);

    @Override
    public void onFileDownloadRequested(@NotNull MonkeyItem item) {
        mockFileNetworkRequests(item);
    }

    @Override
    public void onFileUploadRequested(@NotNull MonkeyItem item) {
        mockFileNetworkRequests(item);
    }

    @Override
    public void onLoadMoreData(int loadedItems) {
        loader.execute();
    }
}