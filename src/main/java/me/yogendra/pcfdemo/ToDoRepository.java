package me.yogendra.pcfdemo;

import static java.lang.Boolean.FALSE;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "todos", collectionResourceRel = "todos")
public interface ToDoRepository extends PagingAndSortingRepository<ToDo, Long> {

  List<ToDo> findByDoneIsFalse(Pageable pageable);

  List<ToDo> findByDoneIsTrue(Pageable pageable);
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
class ToDo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NonNull
  @Column(unique = true)
  private String title;

  @NonNull
  @Column(nullable = false)
  private Boolean done;
}
