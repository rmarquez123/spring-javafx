select
  sr.sensor_id
  , sr.timestamp
  , sr.value
from dev_test1.sensor s
left join dev_test1.sensor_def def
  on def.sensor_def_id = s.sensor_def_id
left join dev_test1.station stn
  on stn.station_id = s.station_id
left join dev_test1.sensor_series sr
  on sr.sensor_id = s.sensor_id
where def.sensor_def_id = :sensorDefId
and stn.station_id = :stnId
and sr."timestamp" between :startDate and :endDate
order by sr."timestamp"
