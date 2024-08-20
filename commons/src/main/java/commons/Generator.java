package commons;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class Generator implements IdentifierGenerator {

    public static final String generatorName = "randomNumber";

    /**
     * This method gets called when an object is added to the database and need an idea.
     * This method checks if it already has an id (id != -1) and generated an id if not so,
     *      otherwise it keeps the same id
     *
     * @param session The session from which the request originates
     * @param object the entity or collection (idbag) for which the id is being generated
     *
     * @return returns the id
     * @throws HibernateException exception
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {

        Serializable id = (Serializable) session.getEntityPersister(null, object)
                .getClassMetadata().getIdentifier(object, session);

        System.out.println((long)id);
        if ((long) id != -1){
            System.out.println("ID was already defined!");
            return id;
        }
        else {
            System.out.println("New ID generated!");
            return this.generateLong(session, object);
        }
    }

    /**
     * Generate a new 6 digit long id
     *
     * @param session The session from which the request originates
     * @param object the entity or collection (idbag) for which the id is being generated
     *
     * @return return the generated id
     */
    public Long generateLong(SharedSessionContractImplementor session, Object object) {
        return new Random().nextLong(100000, 999999);
    }
}