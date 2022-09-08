# !/usr/bin/env python
# -*- coding: utf-8 -*-

from template.base_sql_task import *
import datetime

#
# 目前支持：RUNNER_SPARK_SQL和RUNNER_HIVE
#
sql_runner=RUNNER_STINGER
##sql_runner=RUNNER_HIVE
##sql_runner=RUNNER_SPARK_SQL


def get_customized_items():
    """
     if you need some special values in your sql, please define and calculate then here
     to refer it as {YOUR_VAR} in your sql
    """
    today = Time.today()
    TX_PRE_60_DATE = Time.date_sub(date=today, itv=60)
    TX_PRE_365_DATE = Time.date_sub(date=today, itv=365)
    return locals()

def get_date_range(begin_date, end_date):

    date_list = []
    while begin_date <= end_date:
        date_list.append(begin_date)
        begin_date_object = datetime.datetime.strptime(begin_date, "%Y-%m-%d")
        days1_timedelta = datetime.timedelta(days=1)
        begin_date = (begin_date_object + days1_timedelta).strftime("%Y-%m-%d")
    return date_list

def getMap():
    stand = """
   use dmr_zr;
    insert overwrite table dmrzr_fuxi_daily_intfc_use_i_d partition(dt='{TX_DATE}') 
    select
  z_merchant_no,
  z_project_no,
  case
    when z_merchant_no = '200299' then merchant_name
    else E.name
  end as mc_name,
  pd_merchant_no,
  D.merchant_type,
  z_interface_no,
  intfc_name,
  prod_name,
  prod_type_name,
  q_all,
  q_de,
  q_wu
from
  (
    select
      z_merchant_no,
      z_project_no,
      z_interface_no,
      dt,
      count(1) as q_all,
      sum(chade) as q_de,
      sum(chawu) as q_wu
    from
      (
        select
          z_merchant_no,
          z_project_no,
          z_interface_no,
          case
            when z_response_code = 1000 then 1
            else 0
          end as chade,
          case
            when z_response_code = 1009 then 1
            else 0
          end as chawu,
          dt
        from
          idm.idm_f02_cf_xbxy_fx_inside_profile_i_d
        where
          z_merchant_no not in ('200202','200236','200245','200101','200202')
          and 
          (
            z_parent_interface is null
            or z_interface_no = z_parent_interface
          )
          and dt = '{TX_DATE}'
      ) A
    group by
      z_merchant_no,
      z_project_no,
      z_interface_no,
      dt
  ) A
  left join idm.idm_c02_cf_xbxy_fx_mht_proj_a_d B ON A.z_project_no = B.project_no
  left join dim.dim_c02_cf_xbxy_fx_prod_intfc_a_d C ON A.z_interface_no = C.intfc_code
  left join dmr_add.dmradd_prd_stat_prod_type_a_d D ON A.z_project_no = D.project_no
  left join idm.idm_c01_cf_xbxy_fx_mht_s_d E ON A.z_merchant_no = E.merchant_no and E.dt = '{TX_DATE}';
    """
    sql_map={

     # ATTENTION:   ！！！！ sql_01  因为系统按字典顺序进行排序，小于10 的一定要写成0加编号，否则会顺序混乱，数据出问题，切记，切记！！！！
    "sql_01": stand,
    "sql_02": stand.replace("{TX_DATE}", "2022-08-16")
    }

    dates = get_date_range("2021-01-01", "2022-07-01")

    for index, date in dates:
        key = "sql_" + index
        sql_map[key] = stand.replace("{TX_DATE}", date)

    return sql_map


# 以下部分无需改动，除非作业有特殊要求
sql_task = SqlTask()
sql_task.set_sql_runner(sql_runner)
sql_task.set_customized_items(get_customized_items())
return_code = sql_task.execute_sqls(getMap())
exit(return_code)