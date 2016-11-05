package com.ilya.portfolioproject;



/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 29.09.16 18:36.
 */
public class ViolationListRecyclerAdapter  {
//  OnRecycleCallback onRecycleCallback;
//  private static final int FOOTER_VIEW = 1;
//
//  private List<ViolationItemWrapper> violationItemWrappers;
//  private boolean globalWarn = false;
//  private List<ViolationItem> violationList;
//  private UmortObject umortObject;
//
//  public ViolationListRecyclerAdapter(List<ViolationItem> violationList, UmortObject umortObject,
//      OnRecycleCallback onRecycleCallback) {
//    this.onRecycleCallback = onRecycleCallback;
//    this.violationList = violationList;
//    this.violationItemWrappers = new ArrayList<>(violationList.size() + 1);
//    this.violationItemWrappers.addAll(ViolationItemWrapper.create(violationList));
//    this.umortObject = umortObject;
//  }
//
//  private void addViolation() {
//    ViolationItem violationItem = new ViolationItem();
//    violationItemWrappers.add(ViolationItemWrapper.wrap(violationItem));
//    violationList.add(violationItem);
//    notifyItemInserted(violationItemWrappers.size() - 1);
//    ApplicationHelper.otto.post(
//        new Event.ViolationActUpdated(Event.ViolationActUpdated.What.VIOLATION));
//    ApplicationHelper.otto.post(
//        new Event.ViolationActUpdated(Event.ViolationActUpdated.What.UPDATE_REQUIRED));
//  }
//
//  public void updateWarnUi(boolean warn) {
//    globalWarn = warn;
//    for (ViolationItemWrapper wrapper : violationItemWrappers) {
//      if (wrapper.warnFlag && warn) {
//        notifyItemChanged(violationItemWrappers.indexOf(wrapper));
//      }
//    }
//  }
//
//  public boolean checkRequired() {
//    boolean globalCheck = true;
//    for (ViolationItemWrapper wrapper : violationItemWrappers) {
//      wrapper.violationItem.fastSave();
//      boolean localCheck = true;
//      if (TextUtils.isEmpty(wrapper.violationItem.name)) {
//        localCheck = false;
//      }
//      if (TextUtils.isEmpty(wrapper.violationItem.contractClause)) {
//        localCheck = false;
//      }
//      if (TextUtils.isEmpty(wrapper.violationItem.description)) {
//        localCheck = false;
//      }
//      wrapper.warnFlag = localCheck;
//      globalCheck &= localCheck;
//    }
//    return globalCheck;
//  }
//
//  @Override
//  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//    View v;
//
//    if (viewType == FOOTER_VIEW) {
//      v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_add_violation, parent, false);
//
//      FooterViewHolder vh = new FooterViewHolder(v);
//
//      return vh;
//    }
//
//    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_violation_view, parent, false);
//    ViolationViewHolder vh = new ViolationViewHolder(v);
//    vh.violationName.setOnClickListener(
//        view -> onRecycleCallback.onViolationName(vh.getAdapterPosition()));
//    vh.violationName.setResDrawable(R.drawable.ic_keyboard_arrow_right);
//    vh.deleteViolation.setOnClickListener(view -> {
//      deleteViolation(vh.getAdapterPosition());
//    });
//    vh.violationDescription.setDrawableClickListener(
//        () -> onRecycleCallback.onViolationDescription(vh.getAdapterPosition(),
//            vh.violationDescription));
//    vh.violationDescription.setResDrawable(R.drawable.ic_file);
//    vh.document.setText(umortObject.area);
//    vh.enableTextWatcher(true);
//    return vh;
//  }
//
//  private void deleteViolation(int position) {
//    violationItemWrappers.remove(position);
//    violationList.remove(position);
//    ApplicationHelper.otto.post(
//        new Event.ViolationActUpdated(Event.ViolationActUpdated.What.VIOLATION));
//    ApplicationHelper.otto.post(
//        new Event.ViolationActUpdated(Event.ViolationActUpdated.What.UPDATE_REQUIRED));
//    this.notifyItemRemoved(position);
//  }
//
//  @Override
//  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//    try {
//      if (holder instanceof ViolationViewHolder) {
//        ViolationViewHolder vh = (ViolationViewHolder) holder;
//        vh.enableTextWatcher(false);
//        String typeName = violationItemWrappers.get(position).violationItem.name;
//        String description = violationItemWrappers.get(position).violationItem.description;
//        String clause = violationItemWrappers.get(position).violationItem.contractClause;
//
//        vh.violationName.setText(clause == null ? "" : typeName);
//        vh.contractClause.setText(clause == null ? "" : clause);
//
//        if (description != null && !description.equals("нарушение правил торговли")) {
//          vh.violationDescription.setText(description);
//        } else {
//          vh.violationDescription.setText("");
//        }
//        if (!violationItemWrappers.get(position).warnFlag && globalWarn) {
//          vh.updateWarn();
//        }
//        vh.enableTextWatcher(true);
//      } else if (holder instanceof FooterViewHolder) {
//        FooterViewHolder vh = (FooterViewHolder) holder;
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Override
//  public int getItemCount() {
//    return violationItemWrappers.size() + 1;
//  }
//
//  @Override
//  public int getItemViewType(int position) {
//    if (position == violationItemWrappers.size()) {
//      return FOOTER_VIEW;
//    }
//    return super.getItemViewType(position);
//  }
//
//  private class ViolationViewHolder extends RecyclerView.ViewHolder {
//    protected EditTextWarning violationDescription;
//    protected EditTextWarning violationName;
//    protected EditTextWarning contractClause;
//    protected ImageView deleteViolation;
//    protected TextView document;
//
//    public ViolationViewHolder(View itemLayoutView) {
//      super(itemLayoutView);
//      violationDescription =
//          (EditTextWarning) itemLayoutView.findViewById(R.id.violationDescription);
//      violationName = (EditTextWarning) itemLayoutView.findViewById(R.id.violationName);
//      contractClause = (EditTextWarning) itemLayoutView.findViewById(R.id.contractClause);
//      deleteViolation = (ImageView) itemLayoutView.findViewById(R.id.iv_delete_violation);
//      document = (TextView) itemLayoutView.findViewById(R.id.document);
//    }
//
//    public void enableTextWatcher(boolean enable) {
//      if (enable) {
//        violationName.addTextChangedListener(textWatcher);
//        violationDescription.addTextChangedListener(textWatcher);
//        contractClause.addTextChangedListener(textWatcher);
//      } else {
//        violationName.removeTextChangedListener(textWatcher);
//        violationDescription.removeTextChangedListener(textWatcher);
//        contractClause.removeTextChangedListener(textWatcher);
//      }
//    }
//
//    public void updateWarn() {
//      if (violationName.getText().toString().isEmpty()) violationName.enableIndicator(true);
//      if (violationDescription.getText().toString().isEmpty()) {
//        violationDescription.enableIndicator(true);
//      }
//      if (contractClause.getText().toString().isEmpty()) contractClause.enableIndicator(true);
//    }
//
//    private TextWatcher textWatcher = new TextWatcher() {
//      @Override
//      public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1,
//          final int i2) {
//      }
//
//      @Override
//      public void onTextChanged(final CharSequence charSequence, final int i, final int i1,
//          final int i2) {
//
//      }
//
//      @Override
//      public void afterTextChanged(final Editable editable) {
//        checkTextFields();
//      }
//    };
//
//    private void checkTextFields() {
//      String clause = contractClause.getText().toString();
//      String description = violationDescription.getText().toString();
//      violationItemWrappers.get(this.getAdapterPosition()).violationItem.contractClause = clause;
//      violationItemWrappers.get(this.getAdapterPosition()).violationItem.description = description;
//      ApplicationHelper.otto.post(
//          new Event.ViolationActUpdated(Event.ViolationActUpdated.What.VIOLATION));
//      ApplicationHelper.otto.post(
//          new Event.ViolationActUpdated(Event.ViolationActUpdated.What.UPDATE_REQUIRED));
//    }
//  }
//
//  public class FooterViewHolder extends RecyclerView.ViewHolder {
//    public FooterViewHolder(View itemView) {
//      super(itemView);
//      itemView.setOnClickListener(view -> {
//        addViolation();
//        itemView.requestFocus();
//      });
//    }
//  }
//
//  public interface OnRecycleCallback {
//    public void onViolationName(int position);
//
//    public void onViolationDescription(int position, View view);
//  }
}