package whu.b606.dto;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月14日
 * 
 **/
public enum Status {
	toaudit("待审核", 1), // 刚提交申诉，等待管理员审核通过或拒绝，处于此状态下的任务应当在管理员界面首页醒目提示
	processing("处理中", 2), // 审核通过，若要求专家介入，则已经交付到专家。
	// 到达此状态后，用户方可下载及查看《家用汽车产品三包责任争议第三方调解申请书.doc》
	// 和《家用汽车疑难问题技术咨询申请书.doc（若有专家）》
	// 这意味着，到达此状态后，就应当生成相应的word文档（当然具体的word文档应该在用户点击查看或下载时才予以动态生成）
	// 此外，管理员新建的任务，也应当直接赋予此状态。管理员可在此状态下执行调解终止操作
	// 专家登录后的界面应当显示此状态下的任务（以及由其本人处理过的其它状态的任务），
	// 并可对此状态的任务进行查看，填写结论或咨询意见以及上传处理结果文书，此后将转至“已出咨询意见”状态
	refused("不予受理", 3), // 管理员拒绝了用户的申诉，填写理由后发送邮件，状态不再改变
	terminate("调解终止", 4), // 再分子类： 用户主动撤销，达不成一致，专家无法协调。生成调解终止通知书。状态不再改变
	consulted("已出咨询意见", 5), // 专家出具意见，可能是成功，也可能是无法协调。此后将由管理员将其更改为“调解完成状态”
	complete("调解完成", 6); // 处理完成。
	private String description;
	private int order;

	private Status(String description, int order) {
		this.description = description;
		this.order = order;
	}

	/**
	 * 获得枚举类型实例的描述信息
	 * 
	 * @return
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * 获得状态的优先顺序
	 * 
	 * @return
	 */
	public int getOrder() {
		return this.order;
	}

	/**
	 * 根据描述信息返回对应的枚举实例
	 * 
	 * @param description
	 * @return
	 */
	public static Status string2Enum(String description) {
		Status[] status = Status.values();
		for (Status st : status) {
			String desc = st.getDescription();
			if (description.equalsIgnoreCase(desc.trim())) { return st; }
		}
		return null;
	}
}
