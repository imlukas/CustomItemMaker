package dev.imlukas.customitemmaker.utils.schedulerutil.builders;

import dev.imlukas.customitemmaker.utils.schedulerutil.data.ScheduleBuilderBase;
import dev.imlukas.customitemmaker.utils.schedulerutil.data.ScheduleData;
import dev.imlukas.customitemmaker.utils.schedulerutil.data.ScheduleThread;
import dev.imlukas.customitemmaker.utils.schedulerutil.data.ScheduleTimestamp;
import lombok.Getter;

@Getter
public class RepeatableBuilder extends ScheduleThread implements ScheduleBuilderBase {

    private final ScheduleData data;


    RepeatableBuilder(ScheduleData data) {
        super(data);
        this.data = data;
    }

    public ScheduleTimestamp<ScheduleThread> during(long amount) {
        return new ScheduleTimestamp<>(new ScheduleThread(data), amount, data::setCancelIn);
    }
}
