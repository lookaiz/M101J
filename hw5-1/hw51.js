db.posts.aggregate([

	{"$unwind" : "$comments"},

	{"$project": {_id:0, comments: 1}},

	{"$group": {"_id": "$comments.author", nbComments: {$sum :1}}},

	{"$sort" : {nbComments : -1}},

	{"$limit" : 1}

])
