/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.stream.app.trigger;

import org.springframework.util.StringUtils;

import javax.validation.ValidationException;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Min;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class AbstractTriggerProperties implements TriggerProperties {
	/**
	 * Fixed delay for periodic triggers. Default is 1 TimeUnit.
	 */
	private int fixedDelay = 1;
	/**
	 * Initial delay for periodic triggers. Default is 0.
	 */
	private int initialDelay = 0;
	/**
	 * The TimeUnit to apply to delay values. Default is TimeUnit.SECONDS
	 */
	private TimeUnit timeUnit = TimeUnit.SECONDS;
	/**
	 * The date value for the date trigger.
	 */
	private String date;
	/**
	 * Format for the date value.
	 */
	private String dateFormat = TriggerConstants.DATE_FORMAT;
	/**
	 * Cron expression value for the Cron Trigger.
	 */
	private String cron;

	@Override
	@Min(0)
	public int getInitialDelay() {
		return initialDelay;
	}

	@Override
	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}

	@Override
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	@Override
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	@Override
	public int getFixedDelay() {
		return fixedDelay;
	}

	@Override
	public void setFixedDelay(int fixedDelay) {
		this.fixedDelay = fixedDelay;
	}

	@Override
	public String getCron() {
		return this.cron;
	}

	@Override
	public void setCron(String cron) {
		this.cron = cron;
	}

	@Override
	public Date getDate() {
		if (StringUtils.hasText(this.date)) {
			try {
				return this.getDateFormat().parse(this.date);
			}
			catch (ParseException e) {
				throw new ValidationException("Invalid date value :" + this.date);
			}
		}
		else {
			return null;
		}
	}

	@Override
	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public SimpleDateFormat getDateFormat() {
		if (!StringUtils.hasText(this.dateFormat)) {
			throw new ValidationException("'dateFormat' must not be empty.");
		}
		try {
			return new SimpleDateFormat(this.dateFormat);
		}
		catch (IllegalArgumentException e) {
			throw new ValidationException("Invalid Date format for the string: " + this.dateFormat);
		}
	}

	@Override
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	@AssertFalse
	public boolean isMutuallyExclusive() {
		return this.date != null && this.cron != null && this.fixedDelay != 1;
	}
}
