package com.github.cao.awa.kalmia.network.packet.inbound.invalid.operation;

import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.invalid.operation.OperationInvalidRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.invalid.operation.UnsolvedOperationInvalidPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

/**
 * @param <T>
 * @see OperationInvalidRequest
 * @see UnsolvedOperationInvalidPacket
 */
public class OperationInvalidPacket<T extends PacketHandler<T>> extends ReadonlyPacket<T> {
    private final String reason;

    public OperationInvalidPacket(String reason) {
        this.reason = reason;
    }

    @Override
    public void inbound(RequestRouter router, T handler) {
        System.out.println("Invalid packet because: " + this.reason);
    }
}
