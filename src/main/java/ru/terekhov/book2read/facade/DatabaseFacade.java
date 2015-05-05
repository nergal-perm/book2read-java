package ru.terekhov.book2read.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import ru.terekhov.book2read.control.LibraryFile;
import ru.terekhov.book2read.model.Catalog;
import ru.terekhov.book2read.model.LibraryBook;

@Startup
@Stateless
public class DatabaseFacade {

	private static final Logger logger = Logger.getLogger(LibraryFile.class);
	
	@PersistenceContext(unitName = "Book2Read", type = PersistenceContextType.TRANSACTION)
	private EntityManager em;

	@PostConstruct
	public void initialize() {
		logger.info("Constructed DBFacade, Entity manager is " + (em == null ? "null" : "created successfully"));
	}
	
	public Catalog getCatalogFromDb() {
		CriteriaQuery<Catalog> criteria = em.getCriteriaBuilder().createQuery(
				Catalog.class);
		return em.createQuery(criteria.select(criteria.from(Catalog.class)))
				.getSingleResult();
	}

	public List<LibraryBook> getBooks() {
		if (em != null) {
			CriteriaQuery<LibraryBook> criteria = em.getCriteriaBuilder()
					.createQuery(LibraryBook.class);
			return em.createQuery(criteria.select(criteria.from(LibraryBook.class))).getResultList();
		}

		return new ArrayList<LibraryBook>();
		
	}

	public Date getLastUpdatedDate() {
		  return em.createNamedQuery("Catalog.getLastUpdated", Date.class).getSingleResult();
	}
	
	public void mergeCatalog(Catalog catalog) {
		em.merge(catalog);		
	}
	
	public int clearCatalog() {
		CriteriaDelete<Catalog> cq = em.getCriteriaBuilder().createCriteriaDelete(
				Catalog.class);
		Root<Catalog> root = cq.from(Catalog.class);
		cq.where(root.isNotNull());
		return em.createQuery(cq).executeUpdate();		
	}
	
	public Boolean mergeBook(LibraryBook book) {
		if (book.getLanguage().equalsIgnoreCase("ru") && em.find(LibraryBook.class, book.getId()) == null) {
			em.merge(book);
			return true;
		}
		return false;
	}
}
