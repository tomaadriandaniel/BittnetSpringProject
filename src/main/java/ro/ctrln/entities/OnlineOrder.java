
package ro.ctrln.entities;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class OnlineOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_ID")
    private User user;

    private boolean delivered;
    private boolean returned;
    private boolean cancelled;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OnlineOrderItem> orderItems;


    private double totalPrice;


}
