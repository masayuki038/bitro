package net.wrap_trap.bitro.action;

import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PersistenceManagerProvidor {
	private static final Logger logger = Logger.getLogger(PersistenceManagerProvidor.class.getName());


	private static PersistenceManagerFactory persistentManagerFactory = null;

	private ThreadLocal<PersistenceManager> persistenceManagerContainer = new ThreadLocal<PersistenceManager>();
	
	@Pointcut("execution(@net.wrap_trap.bitro.annotation.Transaction * *.*(..))")
	public void transactionPointcut(){};
	
	@Around("transactionPointcut()")
	public Object doTransaction(ProceedingJoinPoint thisJoinPoint) throws Throwable{
		PersistenceManager pm = persistenceManagerContainer.get();
		if(pm == null){
			pm = getPersistenceManagerFromFactory();
			persistenceManagerContainer.set(pm);
			logger.info("set PersistenceManager to container at " + thisJoinPoint.getSignature().getName());
			Transaction transaction = pm.currentTransaction();
			try{
				transaction.begin();
				Object ret = thisJoinPoint.proceed();
				transaction.commit();
				return ret;
			}catch(Exception e){
				throw e;
			}finally{
				if(transaction.isActive()){
					transaction.rollback();
				}
				pm.close();
				persistenceManagerContainer.set(null);
				logger.info("reset PersistenceManager container at " + thisJoinPoint.getSignature().getName());
			}
		}
		return thisJoinPoint.proceed();
	}
		
	@Around("cflow(transactionPointcut()) && call(* net.wrap_trap.bitro.action.Action.getPersistenceManager())")
	public PersistenceManager getPersistenceManagerAction(){
		return getPersistenceManager();
	}
	
	@Around("cflow(transactionPointcut()) && call(* net.wrap_trap.bitro.model.Model.getPersistenceManager())")
	public PersistenceManager getPersistenceManagerModel(){
		return getPersistenceManager();
	}
	
	protected PersistenceManager getPersistenceManager(){
		PersistenceManager pm = persistenceManagerContainer.get();
		if(pm == null){
			throw new PersistenceManagerProviderException("transaction not started.");
		}
		return pm;
	}
	
	protected PersistenceManager getPersistenceManagerFromFactory(){
		if(persistentManagerFactory == null){
			persistentManagerFactory = JDOHelper.getPersistenceManagerFactory("transactions-optional");
		}
		return persistentManagerFactory.getPersistenceManager();
	}
}
