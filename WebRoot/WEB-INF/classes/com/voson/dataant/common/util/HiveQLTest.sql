select '#TASK_ID#' , 
'#RULE_CODE#' 
from userflow_cdr T where 1=1
{?1=#RULE_CODE#}