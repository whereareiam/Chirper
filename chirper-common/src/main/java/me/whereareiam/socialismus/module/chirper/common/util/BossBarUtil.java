package me.whereareiam.socialismus.module.chirper.common.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.api.model.player.DummyPlayer;
import me.whereareiam.socialismus.api.model.scheduler.DelayedRunnableTask;
import me.whereareiam.socialismus.api.output.Scheduler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;

@Singleton
public class BossBarUtil {
    private final Scheduler scheduler;

    @Inject
    public BossBarUtil(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void createTemporaryBossBar(DummyPlayer dummyPlayer, BossBar bossBar, int duration) {
        Audience audience = dummyPlayer.getAudience();
        audience.showBossBar(bossBar);

        double damagePerSecond = 1.0 / duration;

        for (int i = 1; i <= duration; i++) {
            DelayedRunnableTask task = DelayedRunnableTask.builder()
                    .delay(i * 1000L)
                    .runnable(() -> {
                        if (bossBar.progress() > 0) {
                            bossBar.progress(Math.max(0, bossBar.progress() - (float) damagePerSecond));
                        }
                    })
                    .build();

            scheduler.schedule(task);
        }

        DelayedRunnableTask hideTask = DelayedRunnableTask.builder()
                .delay(duration * 1000L)
                .runnable(() -> audience.hideBossBar(bossBar))
                .build();

        scheduler.schedule(hideTask);
    }
}
