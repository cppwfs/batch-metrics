/*
 * Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.spring.configuration;

import java.util.ArrayList;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class TimestampBatchTaskConfiguration {

	private static final Log logger = LogFactory.getLog(TimestampBatchTaskConfiguration.class);

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;


	@Autowired
	public JobExplorer jobExplorer;

	@Autowired
	public JobRepository jobRepository;

	@Autowired
	public JobLauncher jobLauncher;

	@Autowired
	public JobRegistry jobRegistry;

	@Bean
	public ItemReader<String> reader() {
		List<String> items = new ArrayList<>();
		items.add("*** Test");
		items.add("*** One");
		items.add("*** Two");
		items.add("*** Three");
		return new ListItemReader<>(items);
	}

	@Bean
	public ItemWriter<String> writer() {
		return new ItemWriter<String>() {
			@Override
			public void write(List<? extends String> list) throws Exception {
				for(String s : list) {
//					Thread.sleep(5000);
					System.out.println(s);
				}
			}
		};
	}

	 @Bean
	 public Job job2(Step step1) {
		 return jobBuilderFactory.get("Job2")
				 .incrementer(new RunIdIncrementer())
				 .flow(step1)
				 .end()
				 .build();
	 }
	@Bean
	public Step step1(ItemReader<String> reader, ItemWriter<String> writer) {
		return stepBuilderFactory.get("step1")
				.<String, String> chunk(2)
				.reader(reader())
				.writer(writer)
				.build();
	}

}

