package com.github.cao.awa.kalmia.network.packet.inbound.message.select;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.manage.MessageManager;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.message.select.SelectMessageRequest;
import com.github.cao.awa.kalmia.network.packet.request.message.select.SelectedMessageRequest;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.List;

/**
 * @see SelectMessageRequest
 */
@Server
@AutoSolvedPacket(12)
public class SelectMessagePacket extends ReadonlyPacket<AuthedRequestHandler> {
    private final long sessionId;
    private final long from;
    private final long to;

    public SelectMessagePacket(BytesReader reader) {
        this.sessionId = SkippedBase256.readLong(reader);
        this.from = SkippedBase256.readLong(reader);
        this.to = SkippedBase256.readLong(reader);
    }

    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        long current = this.from;
        int realSelected;

        MessageManager manager = Kalmia.SERVER.messageManager();

        long curSeq = manager.seq(this.sessionId);

        if (current > curSeq) {
            return;
        }

        List<Message> messages = ApricotCollectionFactor.newArrayList(150);

        long to = Math.min(this.to,
                           curSeq
        );

        while (current < to) {
            long selected = Math.min(to - current,
                                     150
            );

            long endSelect = current + selected;

            manager.operation(this.sessionId,
                              current,
                              endSelect,
                              (seq, msg) -> {
                                  messages.add(msg);
                              }
            );

            realSelected = messages.size() - 1;

            router.send(new SelectedMessageRequest(this.sessionId,
                                                   current,
                                                   current + realSelected,
                                                   messages
            ));

            current += selected + 1;

            messages.clear();
        }
    }
}
