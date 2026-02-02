package net.blumasc.selectivepowers.entity.helper;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.phys.Vec3;

public class BossMusicSongPlayer {
    public static final int PLAY_EVENT_INTERVAL_TICKS = 20;
    private long ticksSinceSongStarted;
    @Nullable
    private Holder<JukeboxSong> song;
    private final BlockPos blockPos;
    private final net.minecraft.world.item.JukeboxSongPlayer.OnSongChanged onSongChanged;

    public BossMusicSongPlayer(net.minecraft.world.item.JukeboxSongPlayer.OnSongChanged onSongChanged, BlockPos blockPos) {
        this.onSongChanged = onSongChanged;
        this.blockPos = blockPos;
    }

    public boolean isPlaying() {
        return this.song != null;
    }

    @Nullable
    public JukeboxSong getSong() {
        return this.song == null ? null : (JukeboxSong)this.song.value();
    }

    public long getTicksSinceSongStarted() {
        return this.ticksSinceSongStarted;
    }

    public void setSongWithoutPlaying(Holder<JukeboxSong> song, long ticksSinceSongStarted) {
        if (!((JukeboxSong)song.value()).hasFinished(ticksSinceSongStarted)) {
            this.song = song;
            this.ticksSinceSongStarted = ticksSinceSongStarted;
        }

    }

    public void play(LevelAccessor level, Holder<JukeboxSong> song) {
        this.song = song;
        this.ticksSinceSongStarted = 0L;
        int i = level.registryAccess().registryOrThrow(Registries.JUKEBOX_SONG).getId((JukeboxSong)this.song.value());
        level.levelEvent((Player)null, 1010, this.blockPos, i);
        this.onSongChanged.notifyChange();
    }

    public void stop(LevelAccessor level, @Nullable BlockState state) {
        if (this.song != null) {
            this.song = null;
            this.ticksSinceSongStarted = 0L;
            level.gameEvent(GameEvent.JUKEBOX_STOP_PLAY, this.blockPos, Context.of(state));
            level.levelEvent(1011, this.blockPos, 0);
            this.onSongChanged.notifyChange();
        }

    }

    public void tick(LevelAccessor level, @Nullable BlockState state) {
        if (this.song != null) {
            if (((JukeboxSong)this.song.value()).hasFinished(this.ticksSinceSongStarted)) {
                this.stop(level, state);
            } else {
                if (this.shouldEmitJukeboxPlayingEvent()) {
                    level.gameEvent(GameEvent.JUKEBOX_PLAY, this.blockPos, Context.of(state));
                }

                ++this.ticksSinceSongStarted;
            }
        }

    }

    private boolean shouldEmitJukeboxPlayingEvent() {
        return this.ticksSinceSongStarted % 20L == 0L;
    }

    @FunctionalInterface
    public interface OnSongChanged {
        void notifyChange();
    }
}

