package whu.b606.dto;

import java.util.Date;

import whu.b606.entity.Task;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月9日
 * 
 **/
public class CarTableModel {
	public int taskid;
	public String brand;
	public String model;
	public String carriage;
	public Date dealtime;
	public String description;

	public CarTableModel() {
		super();
	}

	public CarTableModel(int taskid, String brand, String model, String carriage, Date dealtime, String description) {
		super();
		this.taskid = taskid;
		this.brand = brand;
		this.model = model;
		this.carriage = carriage;
		this.dealtime = dealtime;
		this.description = description;
	}

	public CarTableModel(Task t) {
		this.taskid = t.getId();
		this.brand = t.getCar().getBrand();
		this.model = t.getCar().getModel();
		this.carriage = t.getCar().getCarriage();
		this.dealtime = t.getCar().getDealtime();
		this.description = t.getDescription();
	}
}
