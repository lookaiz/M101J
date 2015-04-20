use test
db.zips.aggregate([

	{"$match": { "state" : { "$in": ["CA", "NY"]}}},
	
	{"$group": {_id: {"state":"$state", city:"$city"}, popTotal : {"$sum": "$pop"}}},
	
	{"$match": {"popTotal": {"$gt": 25000}}},

	{"$group": {_id: null, averagePop : {"$avg": "$popTotal"}}},

	{"$project": {_id:0, averagePop : 1}}

])
