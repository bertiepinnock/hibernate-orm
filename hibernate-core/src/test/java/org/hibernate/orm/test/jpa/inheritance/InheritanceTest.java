/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.jpa.inheritance;

import org.hibernate.testing.orm.junit.EntityManagerFactoryScope;
import org.hibernate.testing.orm.junit.Jpa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Emmanuel Bernard
 */
@Jpa(annotatedClasses = {
		Fruit.class,
		Strawberry.class
})
public class InheritanceTest {
	@Test
	public void testFind(EntityManagerFactoryScope scope) {
		scope.inEntityManager(
				entityManager -> {
					Strawberry u = new Strawberry();
					u.setSize( 12l );
					try {
						entityManager.getTransaction().begin();
						entityManager.persist(u);
						entityManager.getTransaction().commit();
						Long newId = u.getId();
						entityManager.clear();

						entityManager.getTransaction().begin();
						// 1.
						Strawberry result1 = entityManager.find(Strawberry.class, newId);
						assertNotNull( result1 );

						// 2.
						Strawberry result2 = (Strawberry) entityManager.find(Fruit.class, newId);

						entityManager.getTransaction().commit();
					}
					catch (Exception e) {
						if ( entityManager.getTransaction().isActive() ) {
							entityManager.getTransaction().rollback();
						}
						throw e;
					}
				}
		);
	}
}
