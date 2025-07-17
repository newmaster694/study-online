package study.online.media.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.online.base.constant.MQConstant;

@Configuration
public class DelayExchangeConfig {

	@Bean
	public DirectExchange delayExchange() {
		return ExchangeBuilder
			.directExchange(MQConstant.DELAY_EXCHANGE_NAME)
			.delayed()
			.durable(true)//持久化
			.build();
	}

	@Bean
	public Queue delayQueue() {
		return new Queue(MQConstant.DELAY_QUEUE_NAME);
	}

	@Bean
	public Binding delayQueueBinding() {
		return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(MQConstant.DELAY_VIDEO_KEY);
	}
}
