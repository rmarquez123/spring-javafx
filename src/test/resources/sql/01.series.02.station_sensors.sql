select
  s.sensor_id 
  , st.station_id 
  , st.station_name
  , sd.sensor_def
  , sd.units
from dev_test1.sensor s
left join dev_test1.sensor_def sd 
  on sd.sensor_def_id = s.sensor_def_id
left join dev_test1.station st
  on st.station_id = s.station_id
and s.duration_id = 0