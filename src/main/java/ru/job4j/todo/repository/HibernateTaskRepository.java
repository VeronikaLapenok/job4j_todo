package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Component
public class HibernateTaskRepository implements TaskRepository {
    private final SessionFactory sf;

    @Autowired
    public HibernateTaskRepository(SessionFactory sf) {
        this.sf = sf;
    }

    @Override
    public Task save(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return task;
    }

    @Override
    public boolean deleteById(int id) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                    "DELETE Task WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return true;
    }

    @Override
    public boolean update(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                    "UPDATE Task SET title = :fTitle, description = :fDescription, "
                            + "created = :fCreated, done = :fDone WHERE id = :fID")
                    .setParameter("fTitle", task.getTitle())
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fCreated", task.getCreated())
                    .setParameter("fDone", task.isDone())
                    .setParameter("fId", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return true;
    }

    @Override
    public List<Task> findAll() {
        Session session = sf.openSession();
        List<Task> resultList = new ArrayList<>();
        try {
            session.beginTransaction();
            resultList = session.createQuery("FROM Task", Task.class)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return resultList;
    }

    @Override
    public Optional<Task> findById(int id) {
        Session session = sf.openSession();
        Optional<Task> resultOptional = Optional.empty();
        try {
            session.beginTransaction();
            resultOptional = session.createQuery(
                    "FROM Task as t WHERE t.id = :fID", Task.class)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return resultOptional;
    }

    @Override
    public Collection<Task> findTasksWhereDoneIsTrue() {
        Session session = sf.openSession();
        List<Task> resultList = new ArrayList<>();
        try {
            session.beginTransaction();
            resultList = session.createQuery(
                    "FROM Task WHERE done = true", Task.class)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return resultList;
    }

    @Override
    public Collection<Task> findTasksWhereDoneIsFalse() {
        Session session = sf.openSession();
        List<Task> resultList = new ArrayList<>();
        try {
            session.beginTransaction();
            resultList = session.createQuery(
                            "FROM Task WHERE done = false", Task.class)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return resultList;
    }
}
