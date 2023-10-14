package ro.ctrln.aspects;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ctrln.enums.Operations;
import ro.ctrln.repositories.UserRepository;
import ro.ctrln.entities.User;
import ro.ctrln.enums.Roles;
import ro.ctrln.exceptions.InvalidCustomerIdException;
import ro.ctrln.exceptions.InvalidOperationException;

import java.util.Collection;
import java.util.Optional;

@Aspect
@Component
@Slf4j
public class SecurityAspect {

    @Autowired
    private UserRepository userRepository;


    @Pointcut("execution(* ro.ctrln.services.ProductService.addProduct(..))")
    public void addProductPointcut(){
    }

    @Pointcut("execution(* ro.ctrln.services.ProductService.updateProduct(..))")
    public void updateProductPointcut(){
    }

    @Pointcut("execution(* ro.ctrln.services.ProductService.deleteProduct(..))")
    public void deleteProductPointcut(){
    }

    @Pointcut("execution(* ro.ctrln.services.ProductService.addStock(..))")
    public void addStockPointcut(){
    }

    @Pointcut("execution(* ro.ctrln.services.OrderService.addOrder(..))")
    public void addOrderPointcut(){
    }

    @Pointcut("execution(* ro.ctrln.services.OrderService.deliverOrder(..))")
    public void deliverOrderPointcut(){
    }

    @Pointcut("execution(* ro.ctrln.services.OrderService.cancelOrder(..))")
    public void cancelOrderPointcut(){
    }

    @Pointcut("execution(* ro.ctrln.services.OrderService.returnOrder(..))")
    public void returnOrderPointcut(){
    }




    @Before("ro.ctrln.aspects.SecurityAspect.addProductPointcut()")
    public void checkSecurityBeforeAddingProduct (JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long customerId = (Long)joinPoint.getArgs()[1];
        User user = getUser(customerId);
        if(userIsNotAllowedOperation(user.getRoles(), Operations.ADD_PRODUCT)){
            throw new InvalidOperationException("User not allowed to add products!");
        }
        log.info("Customer ID: {} will modify the product!", customerId);
    }


    @Before("ro.ctrln.aspects.SecurityAspect.updateProductPointcut()")
    public void checkSecurityBeforeUpdatingProduct (JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long customerId = (Long)joinPoint.getArgs()[1];
        User user = getUser(customerId);
        if(userIsNotAllowedOperation(user.getRoles(), Operations.UPDATE_PRODUCT)){
            throw new InvalidOperationException("User not allowed to update products!");
        }
        log.info("Customer ID: {} will modify the product!", customerId);
    }


    @Before("ro.ctrln.aspects.SecurityAspect.deleteProductPointcut()")
    public void checkSecurityBeforeDeletingProduct (JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long customerId = (Long)joinPoint.getArgs()[1];
        User user = getUser(customerId);
        if(userIsNotAllowedOperation(user.getRoles(), Operations.DELETE_PRODUCT)){
            throw new InvalidOperationException("User not allowed to update products!");
        }
        log.info("Customer ID: {} will modify the product!", customerId);
    }


    @Before("ro.ctrln.aspects.SecurityAspect.addStockPointcut()")
    public void checkSecurityBeforeAddingStock (JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long customerId = (Long)joinPoint.getArgs()[2];
        User user = getUser(customerId);
        if(userIsNotAllowedOperation(user.getRoles(), Operations.ADD_STOCK)){
            throw new InvalidOperationException("User not allowed to add stock for product!");
        }
        log.info("Customer ID: {} will add stock for the product!", customerId);
    }


    @Before("ro.ctrln.aspects.SecurityAspect.addOrderPointcut()")
    public void checkSecurityBeforeAddingOrder (JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long customerId = (Long)joinPoint.getArgs()[1];
        User user = getUser(customerId);
        if(userIsNotAllowedOperation(user.getRoles(), Operations.ADD_ORDER)){
            throw new InvalidOperationException("User not allowed to add an order!");
        }
        log.info("Customer ID: {} will add an order!", customerId);
    }

    @Before("ro.ctrln.aspects.SecurityAspect.deliverOrderPointcut()")
    public void checkSecurityBeforeDeliveringOrder (JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long customerId = (Long)joinPoint.getArgs()[1];
        User user = getUser(customerId);
        if(userIsNotAllowedOperation(user.getRoles(), Operations.DELIVER_ORDER)){
            throw new InvalidOperationException("User not allowed to deliver an order!");
        }
        log.info("Customer ID: {} will deliver an order!", customerId);
    }

    @Before("ro.ctrln.aspects.SecurityAspect.cancelOrderPointcut()")
    public void checkSecurityBeforeCancelingOrder (JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long customerId = (Long)joinPoint.getArgs()[1];
        User user = getUser(customerId);
        if(userIsNotAllowedOperation(user.getRoles(), Operations.CANCEL_ORDER)){
            throw new InvalidOperationException("User not allowed to cancel an order!");
        }
        log.info("Customer ID: {} will cancel an order!", customerId);
    }

    @Before("ro.ctrln.aspects.SecurityAspect.returnOrderPointcut()")
    public void checkSecurityBeforeReturningOrder (JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long customerId = (Long)joinPoint.getArgs()[1];
        User user = getUser(customerId);
        if(userIsNotAllowedOperation(user.getRoles(), Operations.RETURN_ORDER)){
            throw new InvalidOperationException("User not allowed to cancel an order!");
        }
        log.info("Customer ID: {} will cancel an order!", customerId);
    }






    private User getUser(Long customerId) throws InvalidCustomerIdException {
        Optional<User> userOptional = userRepository.findById(customerId);
        if (!userOptional.isPresent()) {
            throw new InvalidCustomerIdException();
        }
        return userOptional.get();
    }


    private boolean userIsNotAllowedOperation(Collection<Roles> roles, Operations operation) {
        switch (operation){
            case ADD_PRODUCT:
            case DELETE_PRODUCT:
            case ADD_STOCK:
                return !roles.contains(Roles.ADMIN);
            case UPDATE_PRODUCT:
                return !roles.contains(Roles.ADMIN) && !roles.contains(Roles.EDITOR);
            case ADD_ORDER:
            case CANCEL_ORDER:
            case RETURN_ORDER:
                return !roles.contains(Roles.CLIENT);
            case DELIVER_ORDER:
                return !roles.contains(Roles.EXPEDITOR);
        }
        return false;

    }


}
