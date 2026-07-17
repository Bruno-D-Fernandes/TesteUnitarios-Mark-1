package edu.jUnitEMosquito.services;

import edu.jUnitEMosquito.dto.task.CreateTaskDTO;
import edu.jUnitEMosquito.dto.task.TaskGroupDto;
import edu.jUnitEMosquito.dto.task.UpdateTaskDTO;
import edu.jUnitEMosquito.dto.task.UpdateTaskStatusDTO;
import edu.jUnitEMosquito.exception.authorization.UsuarioNaoPossuiPermissao;
import edu.jUnitEMosquito.exception.group.UsuarioNaoParticipaDoGrupo;
import edu.jUnitEMosquito.exception.task.GrupoNaoPossuiTasksException;
import edu.jUnitEMosquito.exception.task.TaskNaoEncontradaException;
import edu.jUnitEMosquito.model.Group;
import edu.jUnitEMosquito.model.Task;
import edu.jUnitEMosquito.model.Usuario;
import edu.jUnitEMosquito.model.UsuarioGrupo;
import edu.jUnitEMosquito.repository.TaskRepository;
import edu.jUnitEMosquito.repository.UsuarioGrupoRepository;
import edu.jUnitEMosquito.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    UsuarioGrupoRepository usuarioGrupoRepository;

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskService taskService;

    Usuario usuario;
    Group group;

    @BeforeEach
    void setup() {
        usuario = new Usuario("nome","email","senha");
        usuario.setId(1L);
        group = new Group("g", usuario);
        group.setId(2L);
    }

    @Nested
    class GetTasksByGroupTests {

        @Test
        @DisplayName("retorna lista quando usuário participa e há tasks")
        void returnsTasksWhenUserParticipates() {
            Task task = new Task("t", OffsetDateTime.now(), usuario, group);
            task.setId(5L);

            when(usuarioGrupoRepository.existsByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(true);
            when(taskRepository.findByGrupo_Id(2L)).thenReturn(List.of(task));

            List<TaskGroupDto> result = taskService.getTasksByGroup(usuario, 2L);

            assertEquals(1, result.size());
            assertEquals(task.getId(), result.get(0).id());
        }

        @Test
        @DisplayName("lança exceção quando usuário não participa do grupo")
        void throwsWhenUserNotInGroup() {
            when(usuarioGrupoRepository.existsByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(false);

            assertThrows(UsuarioNaoParticipaDoGrupo.class, () -> taskService.getTasksByGroup(usuario, 2L));
        }

        @Test
        @DisplayName("lança exceção quando não há tasks no grupo")
        void throwsWhenNoTasks() {
            when(usuarioGrupoRepository.existsByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(true);
            when(taskRepository.findByGrupo_Id(2L)).thenReturn(List.of());

            assertThrows(GrupoNaoPossuiTasksException.class, () -> taskService.getTasksByGroup(usuario, 2L));
        }
    }

    @Nested
    class CreateTaskTests {

        @Test
        @DisplayName("cria task quando usuário participa do grupo")
        void createTaskSuccess() {
            UsuarioGrupo ug = new UsuarioGrupo(group, usuario, UsuarioGrupo.Roles.MEMBER);
            when(usuarioGrupoRepository.findByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(Optional.of(ug));

            CreateTaskDTO dto = new CreateTaskDTO("titulo", OffsetDateTime.now(), 2L);

            taskService.createTask(dto, usuario);

            verify(taskRepository, times(1)).save(any(Task.class));
        }

        @Test
        @DisplayName("lança exceção quando usuário não participa do grupo")
        void createTaskUserNotInGroup() {
            when(usuarioGrupoRepository.findByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(Optional.empty());

            CreateTaskDTO dto = new CreateTaskDTO("titulo", OffsetDateTime.now(), 2L);

            assertThrows(UsuarioNaoParticipaDoGrupo.class, () -> taskService.createTask(dto, usuario));
        }
    }

    @Nested
    class UpdateTaskTests {

        @Test
        @DisplayName("atualiza task quando usuário tem permissão")
        void updateTaskSuccess() {
            UsuarioGrupo ug = new UsuarioGrupo(group, usuario, UsuarioGrupo.Roles.MEMBER);
            when(usuarioGrupoRepository.findByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(Optional.of(ug));

            Task task = new Task("old", OffsetDateTime.now(), usuario, group);
            task.setId(10L);
            when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

            UpdateTaskDTO dto = new UpdateTaskDTO(10L, 2L, "novo", OffsetDateTime.now());

            taskService.updateTask(usuario, dto);

            assertEquals("novo", task.getTitle());
        }

        @Test
        @DisplayName("lança exceção quando usuário não participa do grupo")
        void updateTaskUserNotInGroup() {
            when(usuarioGrupoRepository.findByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(Optional.empty());
            UpdateTaskDTO dto = new UpdateTaskDTO(1L, 2L, "x", OffsetDateTime.now());

            assertThrows(UsuarioNaoParticipaDoGrupo.class, () -> taskService.updateTask(usuario, dto));
        }

        @Test
        @DisplayName("lança exceção quando task não encontrada")
        void updateTaskNotFound() {
            UsuarioGrupo ug = new UsuarioGrupo(group, usuario, UsuarioGrupo.Roles.ADMIN);
            when(usuarioGrupoRepository.findByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(Optional.of(ug));
            when(taskRepository.findById(3L)).thenReturn(Optional.empty());

            UpdateTaskDTO dto = new UpdateTaskDTO(3L, 2L, "x", OffsetDateTime.now());

            assertThrows(TaskNaoEncontradaException.class, () -> taskService.updateTask(usuario, dto));
        }

        @Test
        @DisplayName("lança exceção quando usuário não tem permissão")
        void updateTaskNoPermission() {
            Usuario other = new Usuario("outro","o","s");
            other.setId(99L);

            UsuarioGrupo ug = new UsuarioGrupo(group, usuario, UsuarioGrupo.Roles.MEMBER);
            when(usuarioGrupoRepository.findByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(Optional.of(ug));

            Task task = new Task("t", OffsetDateTime.now(), other, group);
            task.setId(11L);
            when(taskRepository.findById(11L)).thenReturn(Optional.of(task));

            UpdateTaskDTO dto = new UpdateTaskDTO(11L, 2L, "x", OffsetDateTime.now());

            assertThrows(UsuarioNaoPossuiPermissao.class, () -> taskService.updateTask(usuario, dto));
        }
    }

    @Nested
    class UpdateTaskStatusTests {

        @Test
        @DisplayName("atualiza status quando usuário participa do grupo")
        void updateStatusSuccess() {
            when(usuarioGrupoRepository.existsByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(true);
            Task task = new Task("t", OffsetDateTime.now(), usuario, group);
            task.setId(20L);
            when(taskRepository.findById(20L)).thenReturn(Optional.of(task));

            UpdateTaskStatusDTO dto = new UpdateTaskStatusDTO(20L, 2L, Task.TaskStatus.FINISHED);

            taskService.updateTaskStatus(usuario, dto);

            assertEquals(Task.TaskStatus.FINISHED, task.getTaskStatus());
        }

        @Test
        @DisplayName("lança exceção quando usuário não participa do grupo")
        void updateStatusUserNotInGroup() {
            when(usuarioGrupoRepository.existsByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(false);
            UpdateTaskStatusDTO dto = new UpdateTaskStatusDTO(1L, 2L, Task.TaskStatus.PAUSED);

            assertThrows(UsuarioNaoParticipaDoGrupo.class, () -> taskService.updateTaskStatus(usuario, dto));
        }

        @Test
        @DisplayName("lança exceção quando task não existe")
        void updateStatusTaskNotFound() {
            when(usuarioGrupoRepository.existsByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(true);
            when(taskRepository.findById(2L)).thenReturn(Optional.empty());

            UpdateTaskStatusDTO dto = new UpdateTaskStatusDTO(2L, 2L, Task.TaskStatus.PAUSED);

            assertThrows(TaskNaoEncontradaException.class, () -> taskService.updateTaskStatus(usuario, dto));
        }
    }

    @Nested
    class DeleteTaskTests {

        @Test
        @DisplayName("deleta task quando usuário participa do grupo")
        void deleteSuccess() {
            when(usuarioGrupoRepository.existsByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(true);
            Task task = new Task("t", OffsetDateTime.now(), usuario, group);
            task.setId(30L);
            when(taskRepository.findById(30L)).thenReturn(Optional.of(task));

            taskService.deleteTask(usuario, 30L, 2L);

            verify(taskRepository, times(1)).delete(task);
        }

        @Test
        @DisplayName("lança exceção quando usuário não participa do grupo")
        void deleteUserNotInGroup() {
            when(usuarioGrupoRepository.existsByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(false);

            assertThrows(UsuarioNaoParticipaDoGrupo.class, () -> taskService.deleteTask(usuario, 1L, 2L));
        }

        @Test
        @DisplayName("lança exceção quando task não existe")
        void deleteTaskNotFound() {
            when(usuarioGrupoRepository.existsByUsuarioAndGrupo_Id(usuario, 2L)).thenReturn(true);
            when(taskRepository.findById(40L)).thenReturn(Optional.empty());

            assertThrows(TaskNaoEncontradaException.class, () -> taskService.deleteTask(usuario, 40L, 2L));
        }
    }
}
