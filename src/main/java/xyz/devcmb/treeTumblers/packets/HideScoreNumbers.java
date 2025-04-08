package xyz.devcmb.treeTumblers.packets;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.score.ScoreFormat;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;

public class HideScoreNumbers implements PacketManipulator {
    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.SCOREBOARD_OBJECTIVE) return;
        WrapperPlayServerScoreboardObjective objective = new WrapperPlayServerScoreboardObjective(event);
        objective.setScoreFormat(ScoreFormat.blankScore());
        event.markForReEncode(true);
    }

    @Override
    public Boolean IsDefault() {
        return true;
    }
}
