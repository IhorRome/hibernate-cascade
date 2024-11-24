package core.basesyntax.dao.impl;

import core.basesyntax.dao.SmileDao;
import core.basesyntax.exception.DataAccessException;
import core.basesyntax.model.Smile;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class SmileDaoImpl extends AbstractDao implements SmileDao {
    public SmileDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Smile create(Smile entity) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(entity);
            entity.setId((Long) session.getIdentifier(entity));
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataAccessException("Can't add smile to DB. Smile: " + entity, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return entity;
    }

    @Override
    public Smile get(Long id) {
        Session session = factory.openSession();
        Smile smile = session.get(Smile.class, id);
        session.close();
        return smile;
    }

    @Override
    public List<Smile> getAll() {
        Session session = factory.openSession();
        List<Smile> smiles = session.createQuery("from Smile",
                        Smile.class)
                .getResultList();
        session.close();
        return smiles;
    }
}
