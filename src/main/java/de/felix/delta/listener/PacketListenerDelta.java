package de.felix.delta.listener;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.events.Listen;
import cc.funkemunky.api.events.impl.PacketLoginEvent;
import cc.funkemunky.api.tinyprotocol.api.Packet;
import cc.funkemunky.api.tinyprotocol.api.TinyProtocolHandler;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInFlyingPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInTransactionPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInWindowClickPacket;
import cc.funkemunky.api.tinyprotocol.packet.login.WrappedHandshakingInSetProtocol;
import cc.funkemunky.api.tinyprotocol.packet.login.WrappedStatusInPing;
import cc.funkemunky.api.tinyprotocol.packet.out.WrappedOutEntityHeadRotation;
import cc.funkemunky.api.tinyprotocol.packet.out.WrappedOutEntityMetadata;
import cc.funkemunky.api.tinyprotocol.packet.out.WrappedOutEntityTeleportPacket;
import cc.funkemunky.api.tinyprotocol.packet.out.WrappedOutExplosionPacket;
import de.felix.delta.DeltaPlugin;
import de.felix.delta.data.DataHolder;

//Need to create an extra class without the implementation of the interface
public class PacketListenerDelta implements AtlasListener {

    @Listen
    public void onLogin(PacketLoginEvent event) {
        switch(event.getPacketType()) {
            case Packet.Login.HANDSHAKE: {
                WrappedHandshakingInSetProtocol packet = new WrappedHandshakingInSetProtocol(event.getPacket());

                break;
            }
            case Packet.Login.PING: {
                WrappedStatusInPing packet = new WrappedStatusInPing(event.getPacket());

                break;
            }
        }
    }

    private cc.funkemunky.api.tinyprotocol.listener.functions.PacketListener otherListener = Atlas.getInstance().getPacketProcessor()
            .process(Atlas.getInstance(), listener -> {

                final DataHolder dataHolder = DeltaPlugin.getInstance().dataManager.getDataHolder(listener.getPlayer());
                if (dataHolder == null) return;
                switch(listener.getType()) {
                    case Packet.Client.TRANSACTION: {
                        WrappedInTransactionPacket packet = new WrappedInTransactionPacket(listener.getPacket(), listener.getPlayer());

                        break;
                    }
                    case Packet.Client.WINDOW_CLICK: {
                        WrappedInWindowClickPacket packet = new WrappedInWindowClickPacket(listener.getPacket(),
                                listener.getPlayer());

                        break;
                    }

                    case Packet.Client.LOOK: {
                        WrappedInFlyingPacket packet = new WrappedInFlyingPacket(listener.getPacket(), listener.getPlayer());

                        break;
                    }

                    case Packet.Client.POSITION: {
                        WrappedInFlyingPacket packet =  new WrappedInFlyingPacket(listener.getPacket(), listener.getPlayer());
                        dataHolder.enemyData.process();
                        break;
                    }

                    case Packet.Client.USE_ENTITY: {
                        WrappedInUseEntityPacket packet = new WrappedInUseEntityPacket(listener.getPacket(),
                                listener.getPlayer());

                        if (packet.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK)
                            dataHolder.enemyData.processHit(packet.getEntity());
                        break;
                    }
                }
            }, Packet.Client.TRANSACTION, Packet.Client.WINDOW_CLICK, Packet.Client.USE_ENTITY, Packet.Client.LOOK, Packet.Client.FLYING, Packet.Client.POSITION);

    private cc.funkemunky.api.tinyprotocol.listener.functions.PacketListener outListener = Atlas.getInstance().getPacketProcessor()
            .process(Atlas.getInstance(), listener -> {
                switch(listener.getType()) {
                    case Packet.Server.EXPLOSION: {
                        WrappedOutExplosionPacket packet = new WrappedOutExplosionPacket(listener.getPacket(), listener.getPlayer());

                        break;
                    }
                    case Packet.Server.HELD_ITEM: {
                        WrappedOutEntityMetadata packet = new WrappedOutEntityMetadata(listener.getPacket(),
                                listener.getPlayer());

                        break;
                    }
                    case Packet.Server.ENTITY_HEAD_ROTATION: {
                        final WrappedOutEntityHeadRotation packet = new WrappedOutEntityHeadRotation(listener.getPacket(), listener.getPlayer());
                        final DataHolder dataHolder = DeltaPlugin.getInstance().dataManager.getDataHolder(listener.getPlayer());
                        if (dataHolder == null) return;
                        dataHolder.rotationData.process(packet);
                        break;
                    }

                    case Packet.Server.KEEP_ALIVE: {
                        WrappedOutEntityTeleportPacket packet = new WrappedOutEntityTeleportPacket(listener.getPacket(),
                                listener.getPlayer());
                        packet.updateObject();
                        break;
                    }
                }
            }, Packet.Server.ENTITY_HEAD_ROTATION, Packet.Server.ENTITY_METADATA, Packet.Server.ENTITY_TELEPORT);


}
