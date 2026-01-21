package study.online.base.constant;

public interface MQConstant {
	String DELAY_EXCHANGE_NAME = "handler.delay.direct";
	String DELAY_QUEUE_NAME = "handler.delay.video.queue";
	String DELAY_VIDEO_KEY = "delay.video.query";

	String FILE_INTERRUPT_EXCHANGE_NAME = "interrupt.direct";
	String FILE_INTERRUPT_QUEUE_NAME = "interrupt.file.queue";
	String FILE_INTERRUPT_KEY = "interrupt.file";
}
