package com.github.cao.awa.kalmia.network.packet.inbound.account.function;

import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.account.function.DisableInstanceRequest;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.cao.awa.kalmia.user.manage.UserManager;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see DisableInstanceRequest
 */
// TODO
@Server
@AutoSolvedPacket(68943)
public class DisableInstancePacket extends ReadonlyPacket<AuthedRequestHandler> {
    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        UserManager manager = Kalmia.SERVER.userManager();
        if (manager.get(handler.getUid()) instanceof DefaultUser user) {
            manager.set(handler.getUid(),
                        user.disable()
            );
        }
        System.out.println("Disabled: " + handler.getUid());
    }
}
