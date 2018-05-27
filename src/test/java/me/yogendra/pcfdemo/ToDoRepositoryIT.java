package me.yogendra.pcfdemo;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.data.domain.Pageable.unpaged;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToDoRepositoryIT {


  @Autowired
  ToDoRepository repository;

  @Before
  public void init() {
    repository.deleteAll();
    List<ToDo> data = asList(
        new ToDo("Learn about PCF", true),
        new ToDo("PCF Demo", false),
        new ToDo("Have Fun", false)
    );
    repository.saveAll(data);

  }

  @Test
  public void checkCount() {
    assertThat(repository.count(), is(3L));
  }

  @Test
  public void checkDone() {
    List<ToDo> data = repository.findByDoneIsTrue(unpaged());
    assertThat(data, notNullValue());
    assertThat(data.size(), is(1));

  }

  @Test
  public void checkNotDone() {
    List<ToDo> data = repository.findByDoneIsFalse(unpaged());
    assertThat(data, notNullValue());
    assertThat(data.size(), is(2));

  }
}
