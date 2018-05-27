package me.yogendra.pcfdemo;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GreetingsControllerTest {

  @Autowired
  MockMvc mvc;

  @Test
  public void greetings() throws Exception {
    mvc.perform(get("/greetings"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(startsWith("Hello, Human. It's ")))
        .andExpect(content().string(endsWith(" here.")));
  }

  @Test
  public void greetingsWithName() throws Exception {
    mvc.perform(get("/greetings?name=Yogi"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(startsWith("Hello, Yogi. It's ")))
        .andExpect(content().string(endsWith(" here.")));
  }

}