package com.mkp.etl.batch

import com.mkp.etl.dto.Product
import com.mkp.etl.listener.JobNotificationListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.support.ListItemReader
import org.springframework.batch.item.support.ListItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate

import javax.sql.DataSource
import java.sql.PreparedStatement

/**
 * Created by pandma on 12/31/17.
 */
@Configuration
@EnableBatchProcessing
class BatchConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    DataSource dataSource

    @Autowired
    JobLauncher jobLauncher

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Bean
    Job testJob(JobNotificationListener jobNotificationListener) {
        return jobBuilderFactory.get("testJob")
                .listener(jobNotificationListener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    Step step1() {
        return stepBuilderFactory.get("step1")
                .<Product, Product> chunk(2)
                .reader(reader())
                .writer(writer())
                .build()
    }

    @Bean
    static ListItemReader<Product> reader() {
        def list = []
        list << new Product('description':'Abc', 'price':1.54)
        list << new Product('description':'Xyz', 'price':2.39)
        list << new Product('description':'123', 'price':3.26)
        logger.info("in reader")
        return new ListItemReader(list)
    }

    @Bean
    public JdbcBatchItemWriter<Product> writer() {
        JdbcBatchItemWriter<Product> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Product>());
        writer.setSql("INSERT INTO product (desciption, price) VALUES (:description, :price)");
        writer.setDataSource(dataSource);
        return writer;
    }
}
